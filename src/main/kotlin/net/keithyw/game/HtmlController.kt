package net.keithyw.game

import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.server.ResponseStatusException

@Controller
class HtmlController(private val repository: GameCharacterRepository) {

    @GetMapping("/")
    fun index(model: Model): String {
        model["title"] = "pee semen"
        return "game"
    }

    @GetMapping("/character/{id}")
    fun gameCharacter(@PathVariable id: Long, model: Model): String {
        val c = repository.findByIdOrNull(id)
            ?.render()
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Character not found")
        model["id"] = id
        model["character"] = c
        return "character"
    }

    fun GameCharacter.render() = RenderedGameCharacter(
        id,
        name,
        race,
        characterClass
    )

    data class RenderedGameCharacter(
        val id: Long?,
        val name: String,
        val race: String,
        val characterClass: String)
}
