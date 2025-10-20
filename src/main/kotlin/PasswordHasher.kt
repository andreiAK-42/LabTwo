import java.security.MessageDigest

class PasswordHasher {
    fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val encodedHash = digest.digest((Salt + password + Salt).toByteArray())

        return encodedHash.fold("") { str, byte -> str + "%02x".format(byte) }
    }
}