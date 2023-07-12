package net.keithyw.game.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.util.StringUtils;
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import io.jsonwebtoken.*;


@Component
class JwtTokenFilter(private val userDetailsService: UserDetailsService): OncePerRequestFilter() {

    @Value("\${game.app.jwt.secret}")
    var jwtSecret: String = ""

//    @Value("\${game.app.jwt.expiration")
//    private var jwtExpiration: Int

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        println("doFilterInternal running")
        filterChain.doFilter(request, response)
    }

    fun parseJwt(request: HttpServletRequest): String? {
        println("parseJwt running")
        val header = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            return header.substring(7, header.length)
        }
        return null
    }

    fun validateJwt(token: String): Boolean {
        println("am i being run")
        try {
            Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token)
            return true
        }
        catch (e: Exception) {
            println("oops " + e.message)
        }
        return false
    }
}