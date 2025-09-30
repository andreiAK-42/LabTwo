data class User(val login: String, val password: String)
data class ResourceAccess(val userLogin: String, val access: String)
data class Resource(val name: String, val accessList: Array<ResourceAccess>, var value: Int, val resources: Array<Resource>?)