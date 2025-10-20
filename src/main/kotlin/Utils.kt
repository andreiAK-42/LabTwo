import kotlin.system.exitProcess

val passwordHasher = PasswordHasher()
val accessControlService = AccessControlService()

fun tryGetUser(login: String, password: String): User {
    val findUser: User? = UserStorage.find { user -> user.login == login }

    if (findUser == null) { exitProcess(ResponseCode.INCORRECT_LOGIN.value) }
    if (findUser.password != passwordHasher.hashPassword(password)) { exitProcess(ResponseCode.INCORRECT_PASSWORD.value) }

    return findUser
}

fun tryGetResource(resourcePath: String, requestedVolume: Int): Resource {
    val resource = getResource(resourcePath)

    if (requestedVolume > resource.value) { exitProcess(ResponseCode.BIG_VALUE.value) }
    if (requestedVolume <= 0) { exitProcess(ResponseCode.BAD_RESOURCE_OR_VALUE.value) }

    resource.value -= requestedVolume

    return resource
}

fun tryDoAction(resource: Resource, user: User, action: String) {
    try {
        val userAccessValue: String? = resource.accessList.find { it.userLogin == user.login }?.access
        accessControlService.checkAccess(userAccessValue,  Action.valueOf(action.uppercase()).ordinal)
    } catch (e: Exception) { exitProcess(ResponseCode.BAD_ACTION.value) }

    if (action.lowercase() == Action.READ.value) { exitProcess(ResponseCode.GET_REPORT.value) }
}

fun getResource(userResourcePath: String): Resource {
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

enum class Action(val value: String) {
    READ("read"),
    WRITE("write"),
    RUN("run")
}

enum class ResponseCode(val value: Int) {
    SUCCESS(0),
    GET_REPORT(1),
    INCORRECT_PASSWORD(2),
    INCORRECT_LOGIN(3),
    BAD_ACTION(4),
    NOT_ACCESS(5),
    BAD_RESOURCE(6),
    BAD_RESOURCE_OR_VALUE(7),
    BIG_VALUE(8);
}
