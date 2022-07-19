package griffio.kollchap.demo

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.jpa.domain.AbstractPersistable
import javax.persistence.Entity
import javax.validation.constraints.NotNull

@Entity(name = "character")
class GameCharacter(
    @get:NotNull val name: String, val background: String
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
