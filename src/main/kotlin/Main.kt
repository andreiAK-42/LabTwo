import models.ResponseCode
import repository.ResourceManager
import services.AccessControlService
import services.PasswordHasher
import services.UserAuthentication
import services.parseArguments
import java.io.PrintStream
import java.nio.charset.StandardCharsets

fun main(args: Array<String>) {
    System.setOut(PrintStream(System.out, true, StandardCharsets.UTF_8))
    val accessControlService = AccessControlService()
    val userAuthentication = UserAuthentication()
    val resourceManager = ResourceManager(accessControlService)
    val arguments = parseArguments(args)

    val (user, userAuthResponseCode) = userAuthentication.tryGetUser(arguments.login, arguments.password)

    if (userAuthResponseCode != ResponseCode.SUCCESS) {
        print(userAuthResponseCode)
    }
    else {
        val (resource, resourceResponseCode) = resourceManager.tryGetResource(arguments.resource, arguments.volume)

        if (resourceResponseCode != ResponseCode.SUCCESS) {
            print(resourceResponseCode)
        }
        else {
            print(resourceManager.tryDoAction(resource!!, user!!, arguments.action, arguments.volume).value)
        }
    }
}