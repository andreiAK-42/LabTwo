package domain

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(
    name = "permissions",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_permissions_user_resource_action",
            columnNames = ["user_id", "resource_id", "action"]
        )
    ]
)
class Permission(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id", nullable = false)
    var resource: Resource,

    @Column(name = "action", nullable = false, length = 16)
    var action: String,

    @Column(name = "created_at", nullable = false)
    var createdAt: Instant = Instant.now()
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}


