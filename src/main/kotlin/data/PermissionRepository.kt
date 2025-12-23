package data

import domain.Permission
import org.springframework.data.jpa.repository.JpaRepository

interface PermissionRepository : JpaRepository<Permission, Long> {

    fun findAllByUserId(userId: Long): List<Permission>
}


