package net.keithyw.game

import javax.persistence.*

@Entity
class GameCharacter(
    var name: String,
    var race: String,
    var characterClass: String,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null
)