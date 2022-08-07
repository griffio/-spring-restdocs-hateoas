package griffio.kollchap.demo

import griffio.kollchap.demo.GameCharacterAlignment.*
import griffio.kollchap.demo.GameCharacterClass.*
import griffio.kollchap.demo.GameCharacterRace.*
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.rest.core.config.RepositoryRestConfiguration
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer
import org.springframework.web.servlet.config.annotation.CorsRegistry

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

@SpringBootApplication
class Application : RepositoryRestConfigurer {

    override fun configureRepositoryRestConfiguration(config: RepositoryRestConfiguration, cors: CorsRegistry?) {
        // https://docs.spring.io/spring-data/rest/docs/current/reference/html/#getting-started.configuration
    }

    @Bean
    fun init(gameCharacterRepository: GameCharacterRepository): CommandLineRunner {
        return CommandLineRunner {
            gameCharacterRepository.save(
                GameCharacter(
                    name = "Slammer Kyntire", race = Human, `class` = Fighter,
                    background = "First-level Fighter searching for the Sword of the Sorcerer.",
                    level = 1, armourClass = 3, hitPoints = 7, alignment = Lawful,
                    characteristics = GameCharacterStats(str = 18, int = 10, wis = 10, dex = 9, con = 11, chr = 10)
                )
            )
            gameCharacterRepository.save(
                GameCharacter(
                    name = "Hotfa Nap", race = Human, `class` = MagicUser,
                    background = "First-level Sorceress from a nomad tribe in the Mesta Desert.",
                    level = 1, armourClass = 9, hitPoints = 5, alignment = Neutral,
                    characteristics = GameCharacterStats(str = 5, int = 10, wis = 8, dex = 18, con = 13, chr = 9)
                )
            )
            gameCharacterRepository.save(
                GameCharacter(
                    name = "Gripper 'The Skin' Longshank", race = Halfling, `class` = Thief,
                    background = "First-level Thief from a tribe on the Albine empire border.",
                    level = 1, armourClass = 4, hitPoints = 5, alignment = Neutral,
                    characteristics = GameCharacterStats(str = 5, int = 10, wis = 8, dex = 18, con = 13, chr = 9)
                )
            )
            gameCharacterRepository.save(
                GameCharacter(
                    name = "Zhod Thobi", race = Human, `class` = Cleric,
                    background = "First-level Cleric N.P.C joins party and receives equal share of treasure.",
                    level = 1, armourClass = 3, hitPoints = 6, alignment = Lawful,
                    characteristics = GameCharacterStats(str = 11, int = 14, wis = 15, dex = 10, con = 7, chr = 10)
                )
            )
            gameCharacterRepository.save(
                GameCharacter(
                    name = "Belisarius", race = Halfling, `class` = Thief,
                    background = "First-level Thief N.P.C survivor. Currently hiding, if located will join party.",
                    level = 1, armourClass = 7, hitPoints = 2, alignment = Neutral,
                    characteristics = GameCharacterStats(str = 5, int = 10, wis = 8, dex = 18, con = 13, chr = 9)
                )
            )
            gameCharacterRepository.save(
                GameCharacter(
                    name = "Rosa Dobbit", race = Halfling, `class` = Fighter,
                    background = "First-level Fighter N.P.C survivor. Currently captive, if released will join party.",
                    level = 1, armourClass = 9, hitPoints = 4, alignment = Lawful,
                    characteristics = GameCharacterStats(str = 8, int = 9, wis = 8, dex = 14, con = 13, chr = 12)
                )
            )
            gameCharacterRepository.save(
                GameCharacter(
                    name = "Odric", race = Human, `class` = Cleric,
                    background = "Third-level Cleric N.P.C enemy. Currently in possession of the stolen Statue of Tranfax.",
                    level = 1, armourClass = 3, hitPoints = 9, alignment = Chaotic,
                    characteristics = GameCharacterStats(str = 5, int = 10, wis = 8, dex = 18, con = 13, chr = 9)
                )
            )
        }
    }

    @Bean
    fun initLocation(roomRepository: DungeonRoomRepository) = CommandLineRunner {
        for (room in rooms) {
            roomRepository.save(
                DungeonRoom(
                    key = room.key,
                    name = room.value.first,
                    description = room.value.second,
                    egress = egress.getOrDefault(room.key, emptyArray())
                )
            )
        }
    }
}

