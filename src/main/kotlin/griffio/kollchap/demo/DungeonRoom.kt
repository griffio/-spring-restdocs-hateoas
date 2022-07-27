package griffio.kollchap.demo

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.Entity
import javax.validation.constraints.NotBlank

@Entity(name = "room")
class DungeonRoom(
    @get:NotBlank val name: String,
    @get:NotBlank val description: String,
) : MappedIdentity<Long>() {
    @JsonIgnore
    override fun getId(): Long? = super.getId()

    @JsonIgnore
    override fun isNew(): Boolean = null == id
}
