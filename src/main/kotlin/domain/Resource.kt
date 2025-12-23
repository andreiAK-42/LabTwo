package domain

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(
    name = "resources",
    uniqueConstraints = [
        UniqueConstraint(name = "uk_resources_title", columnNames = ["title"])
    ]
)
class Resource(
    @Column(name = "title", nullable = false, unique = true, length = 128)
    var title: String,

    @Column(name = "created_at", nullable = false)
    var createdAt: Instant = Instant.now()
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @OneToMany(mappedBy = "resource", cascade = [CascadeType.ALL], orphanRemoval = true)
    var permissions: MutableSet<Permission> = mutableSetOf()
}


