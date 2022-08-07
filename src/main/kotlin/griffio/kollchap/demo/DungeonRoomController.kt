package griffio.kollchap.demo

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.RepresentationModelProcessor
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController()
class DungeonRoomController(
    @Autowired val dungeonRoomRepository: DungeonRoomRepository
) : RepresentationModelProcessor<EntityModel<DungeonRoom>> {
    // add the room egress links to the model using WebMvcLinkBuilder to generate the controller URI
    override fun process(model: EntityModel<DungeonRoom>): EntityModel<DungeonRoom> {
        model.content?.let { room ->
            for (key in room.egress) {
                model.add(linkTo<DungeonRoomController> { roomByKey(key) }.withRel("egress"))
            }
        }
        return model;
    }

    // Adding a custom path to locate a room by its natural map key as the id is considered auto generated
    @GetMapping("/rooms/keys/{key}")
    fun roomByKey(@PathVariable("key") key: String): ResponseEntity<EntityModel<DungeonRoom>> =
        when (val room = dungeonRoomRepository.findByKey(key)) {
            null -> ResponseEntity.notFound().build()
            else -> ResponseEntity.ok(EntityModel.of(room))
        }
}
