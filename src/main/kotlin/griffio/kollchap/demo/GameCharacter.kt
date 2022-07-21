package griffio.kollchap.demo

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.jpa.domain.AbstractPersistable
import javax.persistence.Entity
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

import org.hibernate.validator.constraints.Range
enum class GameCharacterAlignment {
    Chaotic,
    Lawful,
    Neutral
}
@Entity(name = "character")
class GameCharacter(
    @get:NotBlank val name: String,
    @get:NotBlank val background: String,
    @get:Range(min = 1, max = 20) val level: Int,
    @get:Min(0) val armourClass: Int,
    @get:Min(1) val hitPoints: Int,
    @get:NotNull val alignment: GameCharacterAlignment
) : AbstractPersistable<Long>() {
    @JsonIgnore
    override fun getId(): Long? {
        return super.getId()
    }

    @JsonIgnore
    override fun isNew(): Boolean {
        return null == id
    }
}
