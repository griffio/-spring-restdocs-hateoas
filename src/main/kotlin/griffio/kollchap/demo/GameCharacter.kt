package griffio.kollchap.demo

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.jpa.domain.AbstractPersistable
import javax.persistence.Entity
import javax.validation.constraints.NotNull
enum class GameCharacterAlignment {
    Chaotic,
    Lawful,
    Neutral
}
@Entity(name = "character")
class GameCharacter(
    @get:NotNull val name: String,
    @get:NotNull val background: String,
    @get:NotNull val level: Int,
    @get:NotNull val armourClass: Int,
    @get:NotNull val hitPoints: Int,
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
