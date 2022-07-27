package griffio.kollchap.demo

import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.hateoas.MediaTypes
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders
import org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel
import org.springframework.restdocs.hypermedia.HypermediaDocumentation.links
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import javax.servlet.RequestDispatcher

@SpringBootTest(classes = [Application::class])
@AutoConfigureRestDocs
@AutoConfigureMockMvc
class DemoApplicationTests(
    @Autowired val mvc: MockMvc
) {
    @Test
    @Throws(Exception::class)
    fun headersExample() {
        mvc.perform(get("/"))
            .andExpect(status().isOk)
            .andDo(
                document(
                    "headers-example",
                    responseHeaders(
                        headerWithName("Content-Type").description("The Content-Type of the payload `application/hal+json`")
                    )
                )
            )
    }

    @Test
    fun indexExample() {
        mvc.perform(get("/").accept(MediaTypes.HAL_JSON))
            .andExpect(status().isOk)
            .andDo(
                document(
                    "index-example",
                    links(
                        linkWithRel("characters").description("The <<resources_characters,Characters resource>>"),
                        linkWithRel("rooms").description("The <<resources_rooms,Characters resource>>"),
                        linkWithRel("profile").description("The <<resources_profile,ALPS resource>>")
                    ),
                    responseFields(
                        subsectionWithPath("_links").description("<<resources_index_access_links,Links>> to other resources")
                    )
                )
            )
    }

    @Test
    fun errorExample() {
        mvc.perform(
            get("/error")
                .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 400)
                .requestAttr(RequestDispatcher.ERROR_REQUEST_URI, "/characters")
                .requestAttr(RequestDispatcher.ERROR_MESSAGE, "The character '/characters/99999999' does not exist")
        )
            .andDo(MockMvcResultHandlers.print()).andExpect(status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("error", Matchers.`is`("Bad Request")))
            .andExpect(MockMvcResultMatchers.jsonPath("timestamp", Matchers.`is`(Matchers.notNullValue())))
            .andExpect(MockMvcResultMatchers.jsonPath("status", Matchers.`is`(400)))
            .andExpect(MockMvcResultMatchers.jsonPath("path", Matchers.`is`(Matchers.notNullValue())))
            .andDo(
                document(
                    "error-example",
                    responseFields(
                        PayloadDocumentation.fieldWithPath("error").description("The HTTP error that occurred, e.g. `Bad Request`"),
                        PayloadDocumentation.fieldWithPath("path").description("The path to which the request was made"),
                        PayloadDocumentation.fieldWithPath("status").description("The HTTP status code, e.g. `400`"),
                        PayloadDocumentation.fieldWithPath("timestamp").description("The time, in milliseconds, at which the error occurred")
                    )
                )
            )
    }
}
