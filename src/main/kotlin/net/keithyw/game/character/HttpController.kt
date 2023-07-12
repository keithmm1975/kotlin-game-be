package net.keithyw.game.character

import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException


@RestController
@RequestMapping("/api/character/skill")
class CharacterSkillController(private val repository: CharacterSkillRepository) {

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    fun findOne(@PathVariable id: Short) = repository.findByIdOrNull(id) ?: throw
            ResponseStatusException(HttpStatus.NOT_FOUND, "Character Skill Not Found")

}