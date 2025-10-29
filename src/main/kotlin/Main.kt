import models.Resource
import models.ResponseCode
import models.User
import repository.ResourceManager
import services.UserAuthentication
import services.parseArguments
import java.io.PrintStream
import java.nio.charset.StandardCharsets
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    System.setOut(PrintStream(System.out, true, StandardCharsets.UTF_8))
    val userAuthentication = UserAuthentication()
    val resourceManager = ResourceManager()
    val arguments = parseArguments(args)

    val user: User = userAuthentication.tryGetUser(arguments.login, arguments.password)
    val resource: Resource = resourceManager.tryGetResource(arguments.resource, arguments.volume)
    resourceManager.tryDoAction(resource, user, arguments.action)

    exitProcess(ResponseCode.SUCCESS.value)
}
