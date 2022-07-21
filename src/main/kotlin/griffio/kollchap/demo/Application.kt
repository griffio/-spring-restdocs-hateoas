package griffio.kollchap.demo

import griffio.kollchap.demo.GameCharacterAlignment.*
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
                    name = "Slammer Kyntire",
                    background = "First-level Fighter searching for the Sword of the Sorcerer.",
                    level = 1, armourClass = 3, hitPoints = 7, alignment = Lawful
                )
            )
            gameCharacterRepository.save(
                GameCharacter(
                    name = "Hotfa Nap",
                    background = "First-level Sorceress from a nomad tribe in the Mesta Desert.",
                    level = 1, armourClass = 9, hitPoints = 5, alignment = Neutral
                )
            )
            gameCharacterRepository.save(
                GameCharacter(
                    name = "Gripper 'The Skin' Longshank",
                    background = "First-level Thief from a tribe on the Albine empire border.",
                    level = 1, armourClass = 4, hitPoints = 5, alignment = Neutral
                )
            )
            gameCharacterRepository.save(
                GameCharacter(
                    name = "Zhod Thobi",
                    background = "First-level Cleric joins party as N.P.C and receives equal share of treasure.",
                    level = 1, armourClass = 3, hitPoints = 9, alignment = Lawful
                )
            )
            gameCharacterRepository.save(
                GameCharacter(
                    name = "Belisarius",
                    background = "First-level Thief N.P.C survivor. Currently hiding, if located will join party.",
                    level = 1, armourClass = 7, hitPoints = 2, alignment = Neutral
                )
            )
            gameCharacterRepository.save(
                GameCharacter(
                    name = "Rosa Dobbit",
                    background = "First-level Fighter, N.P.C survivor. Currently captive, if released will join party.",
                    level = 1, armourClass = 9, hitPoints = 4, alignment = Lawful
                )
            )
            gameCharacterRepository.save(
                GameCharacter(
                    name = "Odric",
                    background = """Third-level Cleric, N.P.C enemy.
                        | Currently in possession of the stolen Statue of Tranfax.""".trimMargin(),
                    level = 1, armourClass = 3, hitPoints = 9, alignment = Chaotic
                )
            )
        }
    }
}

