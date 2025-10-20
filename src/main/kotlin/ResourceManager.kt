import kotlin.collections.minusAssign
import kotlin.system.exitProcess

class ResourceManager {
    val accessControlService = AccessControlService()

    fun tryGetResource(resourcePath: String, requestedVolume: Int): Resource {
        val resource = getResource(resourcePath)

        if (requestedVolume > resource.value) { exitProcess(ResponseCode.BIG_VALUE.value) }
        if (requestedVolume <= 0) { exitProcess(ResponseCode.BAD_RESOURCE_OR_VALUE.value) }

        resource.value -= requestedVolume

        return resource
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

    fun tryDoAction(resource: Resource, user: User, action: String) {
        try {
            val userAccessValue: String? = resource.accessList.find { it.userLogin == user.login }?.access
            accessControlService.checkAccess(userAccessValue,  Action.valueOf(action.uppercase()).ordinal)
        } catch (e: Exception) { exitProcess(ResponseCode.BAD_ACTION.value) }

        if (action.lowercase() == Action.READ.value) { exitProcess(ResponseCode.GET_REPORT.value) }
    }
}