package net.keithyw.game

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class RepositoriesTests @Autowired constructor(
    val entityManager: TestEntityManager,
    val gameCharacterRepository: GameCharacterRepository) {

    companion object {

        @Container
        private val mysqlContainer = KMysqlContainer(imageName = "mysql:8.0.22")
            .withDatabaseName("kw_game_db")


        init {
            mysqlContainer.start()
        }

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.username", mysqlContainer::getUsername)
            registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl)
            registry.add("spring.datasource.password", mysqlContainer::getPassword)
        }
    }

    @BeforeAll
    fun setup() {
        println(">> Setup")
    }

    @Test
    fun `When findByIdOrNull then return GameCharacter`() {
        var gameCharacter = GameCharacter("Cassandra", "Human", "Cleric")
        entityManager.persist(gameCharacter)
        entityManager.flush()
        val found = gameCharacterRepository.findByIdOrNull(gameCharacter.id!!)
        assertThat(found).isEqualTo(gameCharacter)
    }

    @AfterAll
    fun teardown() {
        println(">> teardown")
    }


}

internal class KMysqlContainer(val imageName: String) : MySQLContainer<KMysqlContainer>(imageName)