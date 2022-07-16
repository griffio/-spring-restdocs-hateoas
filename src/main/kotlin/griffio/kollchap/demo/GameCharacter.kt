package griffio.kollchap.demo

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.jpa.domain.AbstractPersistable
import javax.persistence.Entity

@Entity(name = "character")
class GameCharacter(val name: String, val background: String) : AbstractPersistable<Long>() {
    @JsonIgnore
    override fun getId(): Long? {
        return super.getId()
    }

    @JsonIgnore
    override fun isNew(): Boolean {
        return null == id
    }
}
