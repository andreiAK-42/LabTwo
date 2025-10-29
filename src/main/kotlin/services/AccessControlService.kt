package services

import models.ResponseCode

class AccessControlService {
    fun checkAccess(userAccess: String?, needAccess: Int): ResponseCode {
        if (userAccess == null || userAccess[needAccess] != '7') {
            return ResponseCode.NOT_ACCESS
        }

        return ResponseCode.SUCCESS
    }
}