package net.keithyw.game

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@Testcontainers
class GameApplicationTests {

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

	@Test
	fun contextLoads() {
	}

}
