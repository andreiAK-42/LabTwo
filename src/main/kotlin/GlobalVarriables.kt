val Salt: String = "123456гшщдлоrejhrjhr3otyrfhejk2edwwsfgrt5%^&*()!@#"

val MainResource: Resource = Resource(
    "A", arrayOf(ResourceAccess("alice", "700"), ResourceAccess("root", "777")),
    value = "Главный ресурс сервера",
    resources = arrayOf(
        Resource(
            "A8B", arrayOf(ResourceAccess("alice", "700"), ResourceAccess("root", "777")),
            value = "Program Files",
            resources = arrayOf(
                Resource(
                    "C", arrayOf(ResourceAccess("alice", "707"), ResourceAccess("root", "777")),
                    value = "Secret Documents",
                    resources = arrayOf(
                        Resource(
                        "f_d", arrayOf(ResourceAccess("alice", "777"), ResourceAccess("root", "777")),
                        value = "",
                        resources = null
                    )
                ))
            )
    ))
)