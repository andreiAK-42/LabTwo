package services

import config.SALT
import org.springframework.stereotype.Component
import java.security.MessageDigest

@Component
class PasswordHasher {
    fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val encodedHash = digest.digest((SALT + password + SALT).toByteArray())

        return encodedHash.fold("") { str, byte -> str + "%02x".format(byte) }
    }
}