package net.keithyw.game.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors

@Service
open class UserDetailsServiceImpl(val userRepository: UserRepository) : UserDetailsService {
    @Transactional(readOnly = true)
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username) ?: throw UsernameNotFoundException(username)
        return User(user.username, user.password, roles(user))
    }

    public fun roles(user: net.keithyw.game.user.User) : List<GrantedAuthority>{
        val authorities: List<GrantedAuthority> = user.roles!!.stream().map({ role -> SimpleGrantedAuthority("ROLE_" + role.roleName.toUpperCase())}).collect(Collectors.toList<GrantedAuthority>())
        return authorities
    }
}