package application

import data.PermissionRepository
import data.UserRepository
import domain.Permission
import domain.Resource
import domain.User
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class UserService(
    private val userRepository: UserRepository,
    private val permissionRepository: PermissionRepository
) {

    fun createUser(username: String, passwordHash: String): User {
        val user = User(username = username, passwordHash = passwordHash, createdAt = Instant.now())
        return userRepository.save(user)
    }

    fun findByUsername(username: String): User? = userRepository.findByUsername(username)

    fun getPermissions(userId: Long): List<Permission> = permissionRepository.findAllByUserId(userId)

    fun grantPermission(user: User, resource: Resource, action: String): Permission {
        val permission = Permission(user = user, resource = resource, action = action, createdAt = Instant.now())
        return permissionRepository.save(permission)
    }
}


