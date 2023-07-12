package net.keithyw.game.user

import net.keithyw.game.KMysqlContainer
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
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class RepositoriesTests @Autowired constructor(
    val entityManager: TestEntityManager,
    val userRepository: UserRepository,
    val roleRepository: RoleRepository,
    val permissionRepository: PermissionRepository) {

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
        println(">> User Repositories Test Setup")
    }

    @Test
    fun `When findByIdOrNull then return User`() {
        var admin = User("admin", "test123")
        entityManager.persist(admin)
        entityManager.flush()
        val found = userRepository.findByIdOrNull(admin.id!!)
        assertThat(found).isEqualTo(admin)
    }

    @Test
    fun `When findByPermissionName then return Permission`() {
        var permission = Permission("get")
        entityManager.persist(permission)
        entityManager.flush()
        val perm = permissionRepository.findByPermissionName("get")
        assertThat(perm).isEqualTo(permission)
    }

    @Test
    fun `When findByRoleName then return Role`() {
        var role = Role("user")
        entityManager.persist(role)
        entityManager.flush()
        val r = roleRepository.findByRoleName("user")
        assertThat(r).isEqualTo(role)
    }

    @AfterAll
    fun teardown() {
        println(">> User Repositories Test Teardown")
    }
}