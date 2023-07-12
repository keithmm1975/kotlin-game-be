package net.keithyw.game

import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GameConfiguration {

    @Bean
    fun databaseInitializer(gameCharacterRepository: GameCharacterRepository) = ApplicationRunner {
        val c1 = gameCharacterRepository.save(GameCharacter(
            "Kiira",
            "Elf",
            "Paladin"
        ))
        val c2 = gameCharacterRepository.save(GameCharacter(
            "Yiiri",
            "Elf",
            "Fighter/Mage/Thief"
        ))
        val c3 = gameCharacterRepository.save(GameCharacter(
            "Malindria",
            "Human",
            "druid"
        ))
    }
}