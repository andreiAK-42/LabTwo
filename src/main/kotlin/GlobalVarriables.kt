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