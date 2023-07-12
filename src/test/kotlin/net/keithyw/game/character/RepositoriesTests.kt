package net.keithyw.game.character

import net.keithyw.game.KMysqlContainer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class RepositoriesTests @Autowired constructor(
    val entityManager: TestEntityManager,
    val weaponTypeRepository: WeaponTypeRepository,
    val armorTypeRepository: ArmorTypeRepository,
    val characterSkillRepository: CharacterSkillRepository,
    val raceRepository: RaceRepository,
    val characterClassRepository: CharacterClassRepository,
    val playerCharacterRepository: PlayerCharacterRepository) {

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

    @BeforeAll
    fun setup() {
        println(">> Character Repositories Setup")
    }

    @Test
    fun `When findByIdOrNull then return WeaponType`() {
        var weaponType = WeaponType("sword", WeaponDamageType.SLASHING)
        entityManager.persist(weaponType)
        entityManager.flush()
        val found = weaponTypeRepository.findByIdOrNull(weaponType.id!!)
        assertThat(found).isEqualTo(weaponType)
        assertThat(found?.weaponDamageType).isEqualTo(WeaponDamageType.SLASHING)
    }

    @Test
    fun `When findByIdOrNull then return ArmorType`() {
        var armorType = ArmorType("plate")
        entityManager.persist(armorType)
        entityManager.flush()
        val found = armorTypeRepository.findByIdOrNull(armorType.id!!)
        assertThat(found).isEqualTo(armorType)
    }

    @Test
    fun `When findByIdOrNull then return CharacterSkill`() {
        var characterSkill = CharacterSkill("testskill")
        entityManager.persist(characterSkill)
        entityManager.flush()
        val found = characterSkillRepository.findByIdOrNull(characterSkill.id!!)
        assertThat(found).isEqualTo(characterSkill)
    }

    @Test
    fun `When findByIdOrNull then return Race`() {
        var race = Race("elf")
        entityManager.persist(race)
        entityManager.flush()
        val found = raceRepository.findByIdOrNull(race.id!!)
        assertThat(found).isEqualTo(race)
    }

    @Test
    fun `When findByIdOrNull then return CharacterClass`() {
        var characterClass = CharacterClass("Paladin")
        entityManager.persist(characterClass)
        entityManager.flush()
        val found = characterClassRepository.findByIdOrNull(characterClass.id!!)
        assertThat(found).isEqualTo(characterClass)
    }

    @Test
    fun `When findByIdOrNull then return PlayerCharacter`() {
        var race = Race("Elf")
        entityManager.persist(race)
        entityManager.flush()
        var characterClass = CharacterClass("Paladin")
        entityManager.persist(characterClass)
        entityManager.flush()
        var playerCharacter = PlayerCharacter("Kiira",
            race,
            Gender.FEMALE,
            characterClass
        )
        entityManager.persist(playerCharacter)
        entityManager.flush()
        val found = playerCharacterRepository.findByIdOrNull(playerCharacter.id!!)
        assertThat(found).isEqualTo(playerCharacter)
    }

    @Test
    fun `When adding CharacterSkill PlayerCharacter return CharacterSkill`() {
        var race = Race("Elf")
        entityManager.persist(race)
        entityManager.flush()
        var characterClass = CharacterClass("Paladin")
        entityManager.persist(characterClass)
        entityManager.flush()
        var characterSkill = CharacterSkill("long sword")
        entityManager.persist(characterSkill)
        entityManager.flush()
        var playerCharacter = PlayerCharacter("Kiira",
            race,
            Gender.FEMALE,
            characterClass
        )
        playerCharacter.skills = mutableListOf(characterSkill)
        entityManager.persist(playerCharacter)
        entityManager.flush()
        val found = playerCharacterRepository.findByIdOrNull(playerCharacter.id!!)
        val skill = found?.skills?.get(0)
        assertThat(characterSkill).isEqualTo(skill)
        assertThat(characterSkill.name).isEqualTo(skill?.name)
    }

    @AfterAll
    fun teardown() {
        println(">> Character Repositories Teardown")
    }

}