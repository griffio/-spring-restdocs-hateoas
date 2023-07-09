package griffio.kollchap.demo

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Entity
import jakarta.persistence.Column
import jakarta.validation.constraints.NotBlank

@Entity(name = "room")
class DungeonRoom(
    @get:NotBlank @Column(name = "map_key") val key: String,
    @get:NotBlank val name: String,
    @get:NotBlank val description: String,
    val egress: Array<String>
    ) : MappedIdentity<String>() {
    @JsonIgnore
    override fun getId(): String? = super.getId()

    @JsonIgnore
    override fun isNew(): Boolean = null == id
}
