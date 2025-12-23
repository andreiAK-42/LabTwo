package application

import data.ResourceRepository
import domain.Resource
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class ResourceService(
    private val resourceRepository: ResourceRepository
) {

    fun createResource(title: String): Resource {
        if (resourceRepository.existsByTitle(title)) {
            throw IllegalArgumentException("Resource with title '$title' already exists")
        }
        val resource = Resource(title = title, createdAt = Instant.now())
        return resourceRepository.save(resource)
    }

    fun existsByTitle(title: String): Boolean = resourceRepository.existsByTitle(title)
}


