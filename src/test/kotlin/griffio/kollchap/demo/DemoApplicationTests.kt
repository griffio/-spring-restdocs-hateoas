package griffio.kollchap.demo

import com.fasterxml.jackson.databind.ObjectMapper
import griffio.kollchap.demo.GameCharacterAlignment.*
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.hateoas.MediaTypes
import org.springframework.restdocs.constraints.ConstraintDescriptions
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders
import org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel
import org.springframework.restdocs.hypermedia.HypermediaDocumentation.links
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.snippet.Attributes.key
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import javax.servlet.RequestDispatcher


@SpringBootTest(classes = [Application::class])
@AutoConfigureRestDocs
@AutoConfigureMockMvc
class TestRestDocs(
    @Autowired val objectMapper: ObjectMapper,
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
                        headerWithName("Content-Type").description("The Content-Type of the payload, e.g. `application/hal+json`")
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
                        linkWithRel("profile").description("The <<resources_profile,ALPS resource>>")
                    ),
                    responseFields(
                        subsectionWithPath("_links").description("<<resources_index_access_links,Links>> to other resources")
                    )
                )
            )
    }

    @Test
    fun charactersListExample() {
        mvc.perform(get("/characters").accept(MediaTypes.HAL_JSON))
            .andExpect(status().isOk)
            .andDo(
                document(
                    "characters-list-example",
                    responseFields(
                        subsectionWithPath("_embedded.characters").description("An array of <<resources_character, Character resources>>"),
                        subsectionWithPath("_links").description("<<resources_character_links,Links>> to other resources")
                    )
                )
            )
    }

    @Test
    fun characterGetExample() {
        mvc.perform(get("/characters/1").accept(MediaTypes.HAL_JSON))
            .andExpect(status().isOk)
            .andDo(
                document(
                    "character-get-example",
                    links(
                        linkWithRel("self").description("This <<resources_character,character>>"),
                        linkWithRel("gameCharacter").description("Link to the gameCharacter resource"),
                    ),
                    responseFields(
                        characterFields().plus(
                            subsectionWithPath("_links").description("<<resources_character_links,Links>> to other resources")
                        )
                    )
                )
            )
    }

    @Test
    fun charactersCreateExample() {
        val character = GameCharacter(
            "Bobert",
            "Merchant in the dungeon",
            level = 2,
            armourClass = 3,
            hitPoints = 6,
            alignment = Neutral
        )
        val bobert: String = objectMapper.writeValueAsString(character)
        mvc.perform(post("/characters").contentType("application/json").content(bobert))
            .andExpect(status().isCreated)
            .andDo(
                document(
                    "characters-create-example",
                    requestFields(
                        characterFields()
                    )
                )
            )
            .andReturn().response
            .getHeader("Location")
    }

    @Test
    fun characterUpdateExample() {
        val create = objectMapper.writeValueAsString(GameCharacter("New Character", "New Background", 1, 2, 3, Neutral))

        val characterLocation =
            mvc.perform(post("/characters").contentType("application/json").content(create))
                .andExpect(status().isCreated)
                .andReturn().response.getHeader("Location")

        val update =
            objectMapper.writeValueAsString(GameCharacter("Update name", "Update background", 2, 3, 4, Chaotic))

        mvc.perform(
            patch(characterLocation)
                .contentType(MediaTypes.HAL_JSON)
                .content(update)
        )
            .andExpect(status().isNoContent)
            .andDo(
                document(
                    "character-update-example",
                    requestFields(
                        characterFields()
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
            .andDo(print()).andExpect(status().isBadRequest)
            .andExpect(jsonPath("error", `is`("Bad Request")))
            .andExpect(jsonPath("timestamp", `is`(notNullValue())))
            .andExpect(jsonPath("status", `is`(400)))
            .andExpect(jsonPath("path", `is`(notNullValue())))
            .andDo(
                document(
                    "error-example",
                    responseFields(
                        fieldWithPath("error").description("The HTTP error that occurred, e.g. `Bad Request`"),
                        fieldWithPath("path").description("The path to which the request was made"),
                        fieldWithPath("status").description("The HTTP status code, e.g. `400`"),
                        fieldWithPath("timestamp").description("The time, in milliseconds, at which the error occurred")
                    )
                )
            )
    }

    private fun characterFields(): List<FieldDescriptor> {
        val fields = ConstrainedFields(GameCharacter::class.java)
        return listOf(
            fields.withPath("name")
                .description("Full name of character")
                .type(JsonFieldType.STRING),
            fields.withPath("background")
                .description("Background history and motivation")
                .type(JsonFieldType.STRING),
            fields.withPath("level")
                .description("Experience level")
                .type(JsonFieldType.NUMBER),
            fields.withPath("armourClass")
                .description("Armour protection level")
                .type(JsonFieldType.NUMBER),
            fields.withPath("hitPoints")
                .description("Hit point level")
                .type(JsonFieldType.NUMBER),
            fields.withPath("alignment")
                .description(alignmentDescription())
                .type(JsonFieldType.STRING)
        )
    }

    private fun alignmentDescription(): String =
        GameCharacterAlignment.values().joinToString(prefix = "One of ", separator = ", ")

    private class ConstrainedFields constructor(input: Class<*>) {
        private val constraintDescriptions = ConstraintDescriptions(input)
        fun withPath(path: String): FieldDescriptor {
            return fieldWithPath(path).attributes(
                key("constraints").value(
                    constraintDescriptions.descriptionsForProperty(path).joinToString(". ")
                )
            )
        }
    }
}
