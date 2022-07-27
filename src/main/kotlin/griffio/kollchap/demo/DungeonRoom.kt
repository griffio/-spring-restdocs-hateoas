package griffio.kollchap.demo

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.jpa.domain.AbstractPersistable
import javax.persistence.Entity
import javax.validation.constraints.NotBlank

@Entity(name = "location")
class DungeonLocation(
    @get:NotBlank val name: String,
    @get:NotBlank val description: String,
) : AbstractPersistable<Long>() {
    @JsonIgnore
    override fun getId(): Long? = super.getId()

    @JsonIgnore
    override fun isNew(): Boolean = null == id
}
