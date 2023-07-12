package net.keithyw.game.character

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import net.keithyw.game.KMysqlContainer
import net.keithyw.game.user.Role
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.MediaType
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@RunWith(SpringRunner::class)
@WebMvcTest(CharacterSkillController::class)
@Testcontainers
class HttpControllersTests(@Autowired val mockMvc: MockMvc) {

    @MockkBean
    private lateinit var characterSkillRepository: CharacterSkillRepository

    @MockkBean
    private lateinit var userDetailsService: UserDetailsService

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
    fun `get character skill`() {
        val role = Role("user")
        val authorities: List<GrantedAuthority> = listOf(SimpleGrantedAuthority("ROLE_" + role.roleName.uppercase()))
        val user = User("user", "password123", authorities)
        every { userDetailsService.loadUserByUsername("user") } returns user
        val cs = CharacterSkill("sword")
        every { characterSkillRepository.findByIdOrNull(1)} returns cs
        mockMvc.perform(get("/api/character/skill/1")
//                .with(SecurityMockMvcRequestPostProcessors.httpBasic("user", "test123"))
            .with(jwt().authorities(SimpleGrantedAuthority("ROLE_USER")))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.name").value("sword"))
    }
}