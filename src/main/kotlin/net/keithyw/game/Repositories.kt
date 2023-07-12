package net.keithyw.game

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GameCharacterRepository : JpaRepository<GameCharacter, Long>{}