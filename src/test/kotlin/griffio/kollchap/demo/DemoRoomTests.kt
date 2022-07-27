package griffio.kollchap.demo

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.hateoas.MediaTypes
import org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel
import org.springframework.restdocs.hypermedia.HypermediaDocumentation.links
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(classes = [Application::class])
@AutoConfigureRestDocs
@AutoConfigureMockMvc
class DemoRoomTests(
    @Autowired val objectMapper: ObjectMapper,
    @Autowired val mvc: MockMvc
) {
    @Test
    fun roomsListExample() {
        mvc.perform(get("/rooms").accept(MediaTypes.HAL_JSON))
            .andExpect(status().isOk)
            .andDo(
                document(
                    "rooms-list-example",
                    responseFields(
                        subsectionWithPath("_embedded.rooms")
                            .description("An array of <<resources_room, Room resources>>"),
                        subsectionWithPath("_links")
                            .description("<<resources_room_links,Links>> to other resources")
                    )
                )
            )
    }

    @Test
    fun roomGetExample() {
        mvc.perform(get("/rooms/1").accept(MediaTypes.HAL_JSON))
            .andExpect(status().isOk)
            .andDo(
                document(
                    "room-get-example",
                    links(
                        linkWithRel("self").description("This <<resources_room,room>>"),
                        linkWithRel("dungeonRoom").description("Link to the room resource"),
                    ),
                    responseFields(
                        roomFields().plus(
                            subsectionWithPath("_links").description("<<resources_room_links,Links>> to other resources")
                        )
                    )
                )
            )
    }

    @Test
    fun roomsCreateExample() {
        val create = DungeonRoom(
            "Secret room",
            "Unknown room",
        )
        val room: String = objectMapper.writeValueAsString(create)
        mvc.perform(post("/rooms").contentType("application/json").content(room))
            .andExpect(status().isCreated)
            .andDo(
                document(
                    "rooms-create-example",
                    requestFields(
                        roomFields()
                    )
                )
            )
            .andReturn().response
            .getHeader("Location")
    }

    @Test
    fun roomUpdateExample() {
        val create = objectMapper.writeValueAsString(
            DungeonRoom(
                "New room",
                "New description"
            )
        )

        val roomLocation =
            mvc.perform(post("/rooms").contentType("application/json").content(create))
                .andExpect(status().isCreated)
                .andReturn().response.getHeader("Location")

        val update =
            objectMapper.writeValueAsString(
                DungeonRoom(
                    "Update room",
                    "Update description"
                )
            )

        mvc.perform(
            patch(roomLocation)
                .contentType(MediaTypes.HAL_JSON)
                .content(update)
        )
            .andExpect(status().isNoContent)
            .andDo(
                document(
                    "room-update-example",
                    requestFields(
                        roomFields()
                    )
                )
            )
    }

    private fun roomFields(): List<FieldDescriptor> {
        val fields = ConstrainedFields(DungeonRoom::class.java)
        return listOf(
            fields.withPath("name")
                .description("Name of dungeon room")
                .type(JsonFieldType.STRING),
            fields.withPath("description")
                .description("Content and objects in room")
                .type(JsonFieldType.STRING),
        )
    }
}
