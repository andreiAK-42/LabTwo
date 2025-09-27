import java.security.MessageDigest

fun hashPassword(password: String): String {
    val digest = MessageDigest.getInstance("SHA-256")
    val encodedHash = digest.digest((Salt + password + Salt).toByteArray())

    return encodedHash.fold("") { str, byte -> str + "%02x".format(byte) }
}

fun getUserHashPassword(login: String): User? {
    return UserBase.find { user -> user.login == login }
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