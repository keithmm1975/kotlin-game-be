package net.keithyw.game.user

import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UserConfiguration {

    @Bean
    fun userDatabaseInitializer(userRepository: UserRepository) = ApplicationRunner {
        val u1 = userRepository.save(
            User("admin", "test123")
        )
    }
}