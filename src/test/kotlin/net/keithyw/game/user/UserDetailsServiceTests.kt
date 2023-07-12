package net.keithyw.game.user

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.security.core.userdetails.UsernameNotFoundException

class UserDetailsServiceTests {

    @MockK
    private lateinit var userRepository: UserRepository

    @Test
    fun `When loadUserByUsername then return User`() {
        val role = Role("user")
        val user = User("admin", "test123", mutableListOf(role))
        every { userRepository.findByUsername("admin")} returns user
        val userDetailsService = UserDetailsServiceImpl(userRepository)
        val found = userDetailsService.loadUserByUsername("admin")
        assertThat(found.username).isEqualTo(user.username)
        val foundRole = found.authorities.toMutableList().get(0)
        assertThat(foundRole.authority.toString()).isEqualTo("ROLE_USER")
    }

    @BeforeAll
    fun setup() {
        println(">> User Details Service Test Setup")
        MockKAnnotations.init(this)
    }

    @Test
    fun `When loadUserByUsername with bad user then throw exception`() {
        every { userRepository.findByUsername("admin")} returns null
        val userDetailsService = UserDetailsServiceImpl(userRepository)
        val e = assertThrows<UsernameNotFoundException> ("Should throw an exception") {
            userDetailsService.loadUserByUsername("admin")
        }
        assertEquals("admin", e.message)
    }

    @AfterAll
    fun teardown() {
        println(">> User Repositories Test Teardown")
    }
}