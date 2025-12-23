package domain

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(
    name = "users",
    uniqueConstraints = [
        UniqueConstraint(name = "uk_users_username", columnNames = ["username"])
    ]
)
class User(
    @Column(name = "username", nullable = false, unique = true, length = 64)
    var username: String,

    @Column(name = "password_hash", nullable = false, length = 255)
    var passwordHash: String,

    @Column(name = "created_at", nullable = false)
    var createdAt: Instant = Instant.now()
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var permissions: MutableSet<Permission> = mutableSetOf()
}


