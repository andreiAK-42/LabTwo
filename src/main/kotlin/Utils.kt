import com.sun.tools.javac.Main
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
        println("Ошибка: запрошенный объем ($requestedVolume) превышает доступный (${resource.value})")
        return false
    }

    resource.value -= requestedVolume;
    return true
}

fun findResource(userResourcePath: String): Resource? {
    val pathParts = userResourcePath.split(".")
    if (pathParts.isEmpty()) return null

    if (pathParts[0] != MainResource.name) return null

    var currentResource: Resource = MainResource

    for (i in 1 until pathParts.size) {
        val part = pathParts[i]
        currentResource = currentResource.resources?.find { it.name == part } ?: return null
    }

    return currentResource
}

fun checkAcces(userAcces: String, needAcces: Int): Boolean {
   return userAcces[needAcces] == '7'
}

enum class Action(val value: String) {
    READ("read"),
    WRITE("write"),
    RUN("run")
}

enum class ResponseCode(val value: Int) {
    SUCCES(0),
    GET_REPORT(1),
    INCORRECT_PASSWORD(2),
    INCORRECT_LOGIN(3),
    BAD_ACTION(4),
    NOT_ACCESS(5),
    BAD_RESOURCE(6),
    BAD_RESOURCE_OR_VALUE(7),
    BIG_VALUE(8);
}
