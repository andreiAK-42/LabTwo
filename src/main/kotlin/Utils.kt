import java.security.MessageDigest

fun hashPassword(password: String): String {
    val digest = MessageDigest.getInstance("SHA-256")
    val encodedHash = digest.digest((Salt + password + Salt).toByteArray())

    return encodedHash.fold("") { str, byte -> str + "%02x".format(byte) }
}

fun getUserHashPassword(login: String): User? {
    return UserBase.find { user -> user.login == login }
}

fun processResource(resourcePath: String, requestedVolume: Int): Boolean {
    val resource = findResource(resourcePath)

    if (resource == null) {
        println("Ресурс не найден")
        return false
    }

    if (requestedVolume > resource.value) {
        println("Ошибка: запрошенный объем ($requestedVolume) превышает доступный ($resource.value)")
        return false
    }

    return true
}

fun findResource(userResourcePath: String): Resource? {
    val pathParts = userResourcePath.split(".")
    var currentResource: Resource? = MainResource

    for (part in pathParts) {
        currentResource = currentResource?.resources?.find { it.name == part }
        if (currentResource == null) return null
    }

    return currentResource
}

enum class Action(val value: String) {
    Read("Read"),
    Write("Write"),
    Run("Run")
}

enum class ResponseCode(val value: Int) {
    SUCCES(0),
    GET_REPORT(1),
    INCORRECT_PASSWORD(2),
    INCORRECT_LOGIN(3),
    BAD_ACTION(4),
    NOT_ACCES(5),
    BAD_RESOURCE(6),
    BAD_RESOURCE_OR_VALUE(7),
    BIG_VALUE(8);
}
