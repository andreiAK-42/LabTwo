package services

import models.ResponseCode

open class AccessControlService {
    open fun checkAccess(userAccess: String?, needAccess: Int): ResponseCode {
        if (userAccess == null || userAccess.length <= needAccess || userAccess[needAccess] != '7') {
            return ResponseCode.NOT_ACCESS
        }

        return ResponseCode.SUCCESS
    }
}