package data

import domain.Resource
import org.springframework.data.jpa.repository.JpaRepository

interface ResourceRepository : JpaRepository<Resource, Long> {

    fun existsByTitle(title: String): Boolean
}


