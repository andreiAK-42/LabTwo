package repository

import localStorage.MainResource
import models.Action
import models.Resource
import models.ResponseCode
import models.User
import services.AccessControlService
import kotlin.system.exitProcess

class ResourceManager {
    val accessControlService = AccessControlService()

    fun tryGetResource(resourcePath: String, requestedVolume: Int): Pair<Resource?, ResponseCode> {
        val resource = getResource(resourcePath)

        if (requestedVolume > resource.value) {
            return Pair(resource, ResponseCode.BIG_VALUE)
        }

        if (requestedVolume <= 0) {
            return Pair(resource, ResponseCode.BAD_RESOURCE_OR_VALUE)
        }

        resource.value -= requestedVolume

        return Pair(resource, ResponseCode.SUCCESS)
    }

    private fun getResource(userResourcePath: String): Resource {
        val pathParts = userResourcePath.split(".")
        if (pathParts.isEmpty()) exitProcess(ResponseCode.BAD_RESOURCE.value)

        if (pathParts[0] != MainResource.name) exitProcess(ResponseCode.BAD_RESOURCE.value)

        var currentResource: Resource = MainResource

        for (i in 1 until pathParts.size) {
            val part = pathParts[i]
            currentResource = currentResource.resources?.find { it.name == part } ?: exitProcess(ResponseCode.BAD_RESOURCE.value)
        }

        return currentResource
    }

    fun tryDoAction(resource: Resource, user: User, action: String): ResponseCode {
        try {
            val userAccessValue: String? = resource.accessList.find { it.userLogin == user.login }?.access

            if (accessControlService.checkAccess(userAccessValue, Action.valueOf(action.uppercase()).ordinal) == ResponseCode.SUCCESS) {
                if (action.lowercase() == Action.READ.value) {
                    return ResponseCode.GET_REPORT
                }
                else {
                    return ResponseCode.SUCCESS
                }
            }
            else {
                return ResponseCode.INCORRECT_PASSWORD
            }

        } catch (e: Exception) {
           return ResponseCode.BAD_ACTION
        }
    }
}