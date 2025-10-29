package services

import localStorage.UserStorage
import models.ResponseCode
import models.User

class UserAuthentication {
    val passwordHasher = PasswordHasher()
    fun tryGetUser(login: String, password: String): Pair<User?, ResponseCode> {
        val findUser: User? = UserStorage.find { user -> user.login == login }

        if (findUser == null) {
            return Pair(findUser, ResponseCode.INCORRECT_LOGIN)
        }

        if (findUser.password != passwordHasher.hashPassword(password)) {
            return Pair(findUser,ResponseCode.INCORRECT_PASSWORD)
        }

        return Pair(findUser,ResponseCode.SUCCESS)
    }
}