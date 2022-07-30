package griffio.kollchap.demo

import com.fasterxml.jackson.databind.ObjectMapper
import griffio.kollchap.demo.GameCharacterAlignment.Chaotic
import griffio.kollchap.demo.GameCharacterAlignment.Neutral
import griffio.kollchap.demo.GameCharacterClass.Thief
import griffio.kollchap.demo.GameCharacterRace.Dwarf
import griffio.kollchap.demo.GameCharacterRace.Human
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
import org.springframework.restdocs.payload.RequestBodySnippet
import org.springframework.restdocs.snippet.Attributes
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(classes = [Application::class])
@AutoConfigureRestDocs
@AutoConfigureMockMvc
class DemoCharacterTests(
    @Autowired val objectMapper: ObjectMapper,
    @Autowired val mvc: MockMvc
) {
    @Test
    fun charactersListExample() {
        mvc.perform(get("/characters").accept(MediaTypes.HAL_JSON))
            .andExpect(status().isOk)
            .andDo(
                document(
                    "characters-list-example",
                    responseFields(
                        subsectionWithPath("_embedded.characters")
                            .description("An array of <<resources_character, Character resources>>"),
                        subsectionWithPath("_links")
                            .description("<<resources_character_links,Links>> to other resources")
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
            Human,
            Thief,
            level = 2,
            armourClass = 3,
            hitPoints = 6,
            alignment = Neutral,
            characteristics = rollCharacteristics()
        )
        val bobert: String = objectMapper.writeValueAsString(character)
        mvc.perform(post("/characters").contentType("application/json").content(bobert))
            .andExpect(status().isCreated)
            .andDo(
                document(
                    "characters-create-example",
                    requestFields(
                        characterFields(),
                    )
                )
            ).andDo(
                document(
                    // https://docs.spring.io/spring-restdocs/docs/current/reference/html5/#documenting-your-api-request-response-payloads-subsections-fields
                    // generates a snippet that can be used to document characteristics once
                    "characteristics-create-example",
                    requestFields(
                        beneathPath("characteristics")
                            .withSubsectionId("Characteristics"),
                        characterStatsFields()
                    )
                )
            )
            .andReturn().response
            .getHeader("Location")
    }

    @Test
    fun characterUpdateExample() {
        val create = objectMapper.writeValueAsString(
            GameCharacter(
                "New Character",
                "New Background",
                Human,
                GameCharacterClass.Cleric,
                Neutral,
                1,
                2,
                3,
                characteristics = rollCharacteristics()
            )
        )

        val characterLocation =
            mvc.perform(post("/characters").contentType("application/json").content(create))
                .andExpect(status().isCreated)
                .andReturn().response.getHeader("Location")

        val update =
            objectMapper.writeValueAsString(
                GameCharacter(
                    "Update name",
                    "Update background",
                    Dwarf,
                    GameCharacterClass.Cleric,
                    Chaotic,
                    3,
                    2,
                    1,
                    characteristics = rollCharacteristics()
                )
            )

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

    private fun characterFields(): List<FieldDescriptor> {
        val fields = ConstrainedFields(GameCharacter::class.java)
        return listOf(
            fields.withPath("name")
                .description("Full name of character")
                .type(JsonFieldType.STRING),
            fields.withPath("background")
                .description("Background history and motivation")
                .type(JsonFieldType.STRING),
            fields.withPath("race")
                .description(raceDescription())
                .type(JsonFieldType.STRING),
            fields.withPath("class")
                .description(classDescription())
                .type(JsonFieldType.STRING),
            fields.withPath("level")
                .description("Experience and abilities scale. Higher is better")
                .type(JsonFieldType.NUMBER),
            fields.withPath("armourClass")
                .description("Armour and hit protection scale. Lower is better")
                .type(JsonFieldType.NUMBER),
            fields.withPath("hitPoints")
                .description("Damage a character can take. Higher is better")
                .type(JsonFieldType.NUMBER),
            fields.withPath("alignment")
                .description(alignmentDescription())
                .type(JsonFieldType.STRING),
            //https://docs.spring.io/spring-restdocs/docs/current/reference/html5/#documenting-your-api-request-response-payloads-subsections
            fields.subsectionWithPath("characteristics") // allows fields to be documented separately in characterStatsFields
                .description("<<resources_characteristics_links,STR INT WIS DEX CON CHR>>")
                .type(JsonFieldType.OBJECT)
        )
    }

    private fun characterStatsFields(): List<FieldDescriptor> {
        val fields = ConstrainedFields(GameCharacterStats::class.java)
        return listOf(
            fields.withPath("str")
                .description("Strength attribute")
                .type(JsonFieldType.NUMBER),
            fields.withPath("int")
                .description("Intelligence attribute")
                .type(JsonFieldType.NUMBER),
            fields.withPath("wis")
                .description("Wisdom attribute")
                .type(JsonFieldType.NUMBER),
            fields.withPath("dex")
                .description("Dexterity attribute")
                .type(JsonFieldType.NUMBER),
            fields.withPath("con")
                .description("Constitution attribute")
                .type(JsonFieldType.NUMBER),
            fields.withPath("chr")
                .description("Charisma attribute")
                .type(JsonFieldType.NUMBER)
        )
    }

    private fun alignmentDescription(): String =
        GameCharacterAlignment.values().joinToString(prefix = "One of ", separator = ", ")

    private fun raceDescription(): String =
        GameCharacterRace.values().joinToString(prefix = "One of ", separator = ", ")

    private fun classDescription(): String =
        GameCharacterClass.values().joinToString(prefix = "One of ", separator = ", ")

}
