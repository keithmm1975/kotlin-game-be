package net.keithyw.game.character

import javax.persistence.*

enum class Gender {
    MALE, FEMALE, OTHER
}

enum class WeaponDamageType {
    PIERCING, SLASHING, BLUDGEONING
}

@Entity
class WeaponType (
    var name: String,
    var weaponDamageType: WeaponDamageType,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Short? = null
)

@Entity
class ArmorType (
    var name: String,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Short? = null
)

@Entity
class CharacterSkill (
    var name: String,
    @ManyToMany(mappedBy = "skills", targetEntity = PlayerCharacter::class)
    var playerCharacters: List<PlayerCharacter> = mutableListOf<PlayerCharacter>(),
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Short? = null
)

@Entity
class Race (
    var name: String,
    var bonusStrength: Byte? = 0,
    var bonusIntelligence: Byte? = 0,
    var bonusWisdom: Byte? = 0,
    var bonusDexterity: Byte? = 0,
    var bonusFortitude: Byte? = 0,
    var bonusCharisma: Byte? = 0,
    var bonusWillpower: Byte? = 0,
    var bonusComeliness: Byte? = 0,
    var bonusPerception: Byte? = 0,
    var bonusLife: Short? = 0,
    var bonusMana: Short? = 0,
    @OneToMany(mappedBy = "characterClass", fetch = FetchType.LAZY, targetEntity = PlayerCharacter::class)
    var playerCharacters: List<PlayerCharacter> = mutableListOf<PlayerCharacter>(),
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Short? = null
)

@Entity
class CharacterClass (
    var name: String,
    var minimumStrength: Byte? = 0,
    var minimumIntelligence: Byte? = 0,
    var minimumWisdom: Byte? = 0,
    var minimumDexterity: Byte? = 0,
    var minimumFortitude: Byte? = 0,
    var minimumCharisma: Byte? = 0,
    var minimumWillpower: Byte? = 0,
    var minimumComeliness: Byte? = 0,
    var minimumPerception: Byte? = 0,
    var minimumLife: Short? = 0,
    var minimumMana: Short? = 0,
    @OneToMany(mappedBy = "characterClass", fetch = FetchType.LAZY, targetEntity = PlayerCharacter::class)
    var playerCharacters: List<PlayerCharacter> = mutableListOf<PlayerCharacter>(),
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Short? = null
)

@Entity
class PlayerCharacter (
    var name: String,
    @ManyToOne
    @JoinColumn(name="race_id")
    var race: Race,
    var gender: Gender,
    @ManyToOne
    @JoinColumn(name="character_id")
    var characterClass: CharacterClass,
    var strength: Byte? = 0,
    var intelligence: Byte? = 0,
    var wisdom: Byte? = 0,
    var dexterity: Byte? = 0,
    var fortitude: Byte? = 0,
    var charisma: Byte? = 0,
    var willpower: Byte? = 0,
    var comeliness: Byte? = 0,
    var perception: Byte? = 0,
    var life: Short? = 0,
    var mana: Short? = 0,
    @ManyToMany(cascade = arrayOf(CascadeType.ALL), targetEntity = CharacterSkill::class)
    @JoinTable(name="character_skills",
        joinColumns = arrayOf(JoinColumn(name="character_skill_id", referencedColumnName = "id")),
        inverseJoinColumns = arrayOf(JoinColumn(name="player_character_id", referencedColumnName = "id"))
    )
    var skills: List<CharacterSkill> = mutableListOf<CharacterSkill>(),
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Short? = null
)