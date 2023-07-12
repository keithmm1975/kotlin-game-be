package net.keithyw.game

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.HttpStatus
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.assertj.core.api.Assertions.assertThat as assertThat

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class IntegrationTests(@Autowired val restTemplate: TestRestTemplate) {

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
    fun `Assert game page title, content and status code`() {
        val entity = restTemplate.withBasicAuth("user", "test123")
                .getForEntity<String>("/")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).contains("<h1>I have a huge penis</h1>")
    }

    @Test
    fun `Assert character page, content and status code`() {
        val entity = restTemplate.withBasicAuth("user", "test123")
                .getForEntity<String>("/character/1")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).contains("<b>Kiira</b>")
    }

    @AfterAll
    fun teardown() {
        println(">> Tear Down")
    }

}