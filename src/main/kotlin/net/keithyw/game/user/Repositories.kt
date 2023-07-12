package net.keithyw.game.user

import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, Int> {
    fun findByUsername(username: String): User?
}

interface RoleRepository : CrudRepository<Role, Int> {
    fun findByRoleName(roleName: String): Role?
}

interface PermissionRepository : CrudRepository<Permission, Int> {
    fun findByPermissionName(permissionName: String): Permission?
}