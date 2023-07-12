package net.keithyw.game.character

import org.springframework.data.repository.CrudRepository

interface WeaponTypeRepository : CrudRepository<WeaponType, Short>{}
interface ArmorTypeRepository : CrudRepository<ArmorType, Short>{}
interface CharacterSkillRepository : CrudRepository<CharacterSkill, Short>{}
interface RaceRepository : CrudRepository<Race, Short>{}
interface CharacterClassRepository : CrudRepository<CharacterClass, Short>{}
interface PlayerCharacterRepository : CrudRepository<PlayerCharacter, Short>{}

