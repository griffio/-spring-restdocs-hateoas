package griffio.kollchap.demo

import org.springframework.data.repository.CrudRepository
import org.springframework.data.rest.core.annotation.Description
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.data.rest.core.annotation.RestResource
import org.springframework.stereotype.Component

@Component // annotation enables Intellij to detect bean
@RepositoryRestResource(collectionResourceRel = "rooms", path = "rooms")
interface DungeonRoomRepository : CrudRepository<DungeonRoom, String> {
    // All query method resources are exposed under the "/search" resource
    // can be configured to /rooms/search/keys?key=1.
    @RestResource(path = "keys", rel = "keys", description = Description("locate room by map key"))
    fun findByKey(key: String): List<DungeonRoom>?
}
