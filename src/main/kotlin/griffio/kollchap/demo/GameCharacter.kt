package griffio.kollchap.demo

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.Entity
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

import org.hibernate.validator.constraints.Range
import javax.persistence.Embeddable
import javax.persistence.Embedded

enum class GameCharacterAlignment {
    Chaotic,
    Lawful,
    Neutral
}

enum class GameCharacterRace {
    Dwarf,
    Elf,
    Halfling,
    Human
}

enum class GameCharacterClass {
    Cleric,
    Fighter,
    MagicUser,
    Thief
}

@Entity(name = "character")
class GameCharacter(
    @get:NotBlank val name: String,
    @get:NotBlank val background: String,
    @get:NotNull val race: GameCharacterRace,
    @get:NotNull val `class`: GameCharacterClass,
    @get:NotNull val alignment: GameCharacterAlignment,
    @get:Range(min = 1, max = 20) val level: Int,
    @get:Range(min = 0, max = 9) val armourClass: Int,
    @get:Min(0) val hitPoints: Int,
    @Embedded val characteristics: GameCharacterStats
) : MappedIdentity<Long>() {
    @JsonIgnore
    override fun getId(): Long? = super.getId()

    @JsonIgnore
    override fun isNew(): Boolean = null == id
}

@Embeddable
class GameCharacterStats(
    @get:Range(min = 3, max = 18) val str: Int,
    @get:Range(min = 3, max = 18) val int: Int,
    @get:Range(min = 3, max = 18) val wis: Int,
    @get:Range(min = 3, max = 18) val dex: Int,
    @get:Range(min = 3, max = 18) val con: Int,
    @get:Range(min = 3, max = 18)val chr: Int
)

val threeDSix = 3..18

fun rollCharacteristics(): GameCharacterStats {
    return GameCharacterStats(
        str = threeDSix.random(),
        int = threeDSix.random(),
        wis = threeDSix.random(),
        dex = threeDSix.random(),
        con = threeDSix.random(),
        chr = threeDSix.random()
    )
}
