import kotlin.system.exitProcess

class AccessControlService {
    fun checkAccess(userAccess: String?, needAccess: Int): Boolean {
        if (userAccess == null || userAccess[needAccess] != '7') {
            exitProcess(ResponseCode.NOT_ACCESS.value)
        }
        else {
            return true
        }
    }
}
