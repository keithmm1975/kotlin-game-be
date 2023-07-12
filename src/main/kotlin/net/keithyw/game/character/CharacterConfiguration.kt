package net.keithyw.game.character

import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CharacterConfiguration {

    @Bean
    fun characterDatabaseInitializer(
        characterSkillRepository: CharacterSkillRepository
    ) = ApplicationRunner  {
        val cs1 = characterSkillRepository.save(
            CharacterSkill("sword")
        )
    }

}