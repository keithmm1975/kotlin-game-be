package net.keithyw.game.config

import io.jsonwebtoken.security.Keys
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.http.HttpHeaders
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.test.context.junit4.SpringRunner
import io.jsonwebtoken.*
import io.jsonwebtoken.io.Encoders
import java.security.Key;
import javax.crypto.spec.SecretKeySpec
import javax.xml.bind.DatatypeConverter


@RunWith(SpringRunner::class)
class JwtTokenFilterTests {

    private lateinit var userDetailsService: UserDetailsService

    @BeforeAll
    fun setup() {
        val user = User("user", "password123", emptyList())
        userDetailsService = mockk<UserDetailsService>()
        every { userDetailsService.loadUserByUsername("user") } returns user
    }

    @Test
    fun `Has Jwt Token`() {
        val filter = JwtTokenFilter(userDetailsService)
        val req = MockHttpServletRequest()
        val token = "test123"
        req.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        val jwt = filter.parseJwt(req)
        assertThat(jwt).isEqualTo(token)
    }

    @Test
    fun `Has Wrong Header`() {
        val filter = JwtTokenFilter(userDetailsService)
        val req = MockHttpServletRequest()
        req.addHeader(HttpHeaders.AUTHORIZATION, "")
        val jwt = filter.parseJwt(req)
        assertThat(jwt).isNull()
    }

    @Test
    fun `Jwt Token Is Null`() {
        val filter = JwtTokenFilter(userDetailsService)
        val req = MockHttpServletRequest()
        val jwt = filter.parseJwt(req)
        assertThat(jwt).isNull()
    }

    @Test
    fun `Jwt Token Is Valid`() {
        val filter = JwtTokenFilter(userDetailsService)
        val secret = "oeRaYY7Wo24sDqKSX3IM9ASGmdGPmkTd9jo1QTy4b7P9Ze5_9hKolVX8xNrQDcNRfVEdTZNOuOyqEGhXEbdJI-ZQ19k_o9MI0y3eZN2lp9jow55FfXMiINEdt1XR85VipRLSOkT6kSpzs2x-jbLDiz9iFVzkd81YKxMgPA7VfZeQUm4n-mOmnWMaVX30zGFU4L3oPBctYKkl4dYfqYWqRNfrgPJVi5DGFjywgxx0ASEiJHtV72paI3fDR2XwlSkyhhmY-ICjCRmsJN4fX1pdoL8a18-aQrvyu4j0Os6dVPYIoPvvY0SAZtWYKHfM15g7A3HD4cVREf9cUsprCRK93w"
        val bytes = DatatypeConverter.parseBase64Binary(secret)
        val signingKey = SecretKeySpec(bytes, SignatureAlgorithm.HS256.jcaName)
        val token = Jwts.builder()
            .setSubject(bytes.toString())
            .signWith(signingKey)
            .compact()
        filter.jwtSecret = Encoders.BASE64.encode(signingKey.encoded)
        assertThat(filter.validateJwt(token)).isTrue()
    }

    @Test
    fun `Jwt Token Is Not Valid`() {
        val filter = JwtTokenFilter(userDetailsService)
        val token = ""
        assertThat(filter.validateJwt(token)).isFalse()

    }
}