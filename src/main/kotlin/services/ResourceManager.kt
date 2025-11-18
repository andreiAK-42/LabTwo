package services

import models.Action
import models.Resource
import models.ResponseCode
import models.User
import repository.sqlite.getResourceByPath
import repository.sqlite.updateResourceValue
import repository.sqlite.userExists
import repository.sqlite.resourceExists as repoResourceExists

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
        if (pathParts.isEmpty()) {
            return Pair(null, ResponseCode.BAD_RESOURCE)
        }

        val (exists, existsCode) = repoResourceExists(pathParts[0])
        if (existsCode != ResponseCode.SUCCESS || !exists) {
            return Pair(null, ResponseCode.BAD_RESOURCE)
        }

        val (resource, resourceCode) = getResourceByPath(pathParts)
        if (resourceCode != ResponseCode.SUCCESS || resource == null) {
            return Pair(null, ResponseCode.BAD_RESOURCE)
        }

        return Pair(resource, ResponseCode.SUCCESS)
    }

    fun tryDoAction(resource: Resource, user: User, action: String, volume: Int): ResponseCode {
        val (userExists, userExistsCode) = userExists(user.login)
        if (userExistsCode != ResponseCode.SUCCESS || !userExists) {
            return ResponseCode.NOT_ACCESS
        }

        val actionEnum = try {
            Action.valueOf(action.uppercase())
        } catch (e: IllegalArgumentException) {
            return ResponseCode.BAD_ACTION
        }

        val userAccessValue: String? = resource.accessList.find { it.userLogin == user.login }?.access

        if (accessControlService.checkAccess(userAccessValue, actionEnum.ordinal) != ResponseCode.SUCCESS) {
            return ResponseCode.NOT_ACCESS
        }

        val newValue = resource.value - volume
        val updateResult = updateResourceValue(resource.name, newValue)

        if (updateResult != ResponseCode.SUCCESS) {
            return updateResult
        }

        resource.value = newValue

        return if (actionEnum == Action.READ) {
            ResponseCode.GET_REPORT
        } else {
            ResponseCode.SUCCESS
        }
    }
}