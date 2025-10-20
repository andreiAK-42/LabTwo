import kotlin.system.exitProcess

class UserAuthentication {
    val passwordHasher = PasswordHasher()
    fun tryGetUser(login: String, password: String): User {
        val findUser: User? = UserStorage.find { user -> user.login == login }

        if (findUser == null) { exitProcess(ResponseCode.INCORRECT_LOGIN.value) }
        if (findUser.password != passwordHasher.hashPassword(password)) { exitProcess(ResponseCode.INCORRECT_PASSWORD.value) }

        return findUser
    }
}