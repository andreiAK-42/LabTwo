package repository

import localStorage.MainResource
import models.Action
import models.Resource
import models.ResponseCode
import models.User
import services.AccessControlService
import kotlin.system.exitProcess

class ResourceManager(private val accessControlService: AccessControlService) {

    fun tryGetResource(resourcePath: String, requestedVolume: Int): Pair<Resource?, ResponseCode> {
        val (resource, responseCode) = getResource(resourcePath)

        if (responseCode != ResponseCode.SUCCESS) {
            return Pair(null, responseCode)
        }

        resource!!

        if (requestedVolume > resource.value) {
            return Pair(resource, ResponseCode.BIG_VALUE)
        }

        if (requestedVolume <= 0) {
            return Pair(resource, ResponseCode.BAD_RESOURCE_OR_VALUE)
        }

        return Pair(resource, ResponseCode.SUCCESS)
    }

    private fun getResource(userResourcePath: String): Pair<Resource?, ResponseCode> {
        val pathParts = userResourcePath.split(".")
        if (pathParts.isEmpty() || pathParts[0] != MainResource.name) {
            return Pair(null, ResponseCode.BAD_RESOURCE)
        }

        var currentResource: Resource = MainResource

        for (i in 1 until pathParts.size) {
            val part = pathParts[i]
            val foundResource = currentResource.resources?.find { it.name == part }
            if (foundResource == null) {
                return Pair(null, ResponseCode.BAD_RESOURCE)
            }
            currentResource = foundResource
        }

        return Pair(currentResource, ResponseCode.SUCCESS)
    }

    fun tryDoAction(resource: Resource, user: User, action: String, volume: Int): ResponseCode {
        val actionEnum = try {
            Action.valueOf(action.uppercase())
        } catch (e: IllegalArgumentException) {
            return ResponseCode.BAD_ACTION
        }

        val userAccessValue: String? = resource.accessList.find { it.userLogin == user.login }?.access

        if (accessControlService.checkAccess(userAccessValue, actionEnum.ordinal) != ResponseCode.SUCCESS) {
            return ResponseCode.NOT_ACCESS
        }

        resource.value -= volume

        return if (actionEnum == Action.READ) {
            ResponseCode.GET_REPORT
        } else {
            ResponseCode.SUCCESS
        }
    }
}
