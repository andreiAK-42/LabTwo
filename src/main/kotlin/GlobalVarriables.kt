val Salt: String = "123456гшщдлоrejhrjhr3otyrfhejk2edwwsfgrt5%^&*()!@#"

val MainResource: Resource = Resource(
    "A", arrayOf(ResourceAccess("alice", "700"), ResourceAccess("root", "777")),
    value = 250,
    resources = arrayOf(
        Resource(
            "A8B", arrayOf(ResourceAccess("alice", "700"), ResourceAccess("root", "777")),
            value = 56,
            resources = arrayOf(
                Resource(
                    "C", arrayOf(ResourceAccess("alice", "707"), ResourceAccess("root", "777")),
                    value = 12,
                    resources = arrayOf(
                        Resource(
                        "f_d", arrayOf(ResourceAccess("alice", "777"), ResourceAccess("root", "777")),
                        value = 3,
                        resources = null
                    )
                ))
            )
    ))
)

val UserStorage: List<User> = listOf(
    User("alice", "67a683888f969f39637981b7f06e6403bacc667b31d0323a0880d5acdcf136b4"),
    User("root", "af2013c1bb9daa12bec7d226a0d06921afdbfabdef4767f7aab1ba0fcb3e6e4c")
)
