package net.keithyw.game.user

import net.keithyw.game.character.CharacterSkill
import net.keithyw.game.character.PlayerCharacter
import javax.persistence.*
import org.springframework.security.core.userdetails.UserDetails;

@Entity
class User (
    var username: String,
    var password: String,
    @ManyToMany(mappedBy = "users", targetEntity = Role::class)
    var roles: List<Role> = mutableListOf<Role>(),
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Int? = null
)

@Entity
class Role (
    var roleName: String,
    @ManyToMany(cascade = arrayOf(CascadeType.ALL), targetEntity = Permission::class)
    @JoinTable(name="role_permissions",
        joinColumns = arrayOf(JoinColumn(name="permission_id", referencedColumnName = "id")),
        inverseJoinColumns = arrayOf(JoinColumn(name="role_id", referencedColumnName = "id"))
    )
    var permissions: List<Permission> = mutableListOf<Permission>(),
    @ManyToMany(cascade = arrayOf(CascadeType.ALL), targetEntity = User::class)
    @JoinTable(name="user_roles",
        joinColumns = arrayOf(JoinColumn(name="user_id", referencedColumnName = "id")),
        inverseJoinColumns = arrayOf(JoinColumn(name="role_id", referencedColumnName = "id"))
    )
    var users: List<User> = mutableListOf<User>(),
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Int? = null
)

@Entity
class Permission (
    var permissionName: String,
    @ManyToMany(mappedBy = "permissions", targetEntity = Role::class)
    var roles: List<Role> = mutableListOf<Role>(),
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Int? = null
)