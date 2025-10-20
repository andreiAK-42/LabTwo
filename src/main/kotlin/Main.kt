import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.required
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

fun parseArguments(args: Array<String>): Arguments {
    val parser = ArgParser("LabTwo")

    val login by parser.option(
        ArgType.String,
        description = "Логин пользователя",
        fullName = "login"
    ).required()

    val password by parser.option(
        ArgType.String,
        description = "Пароль пользователя",
        fullName = "password"
    ).required()

    val action by parser.option(
        ArgType.String,
        description = "Виды действий: \n" +
                      "        1. read - Попытка чтение ресурса)\n" +
                      "        2. write - Попытка редактирования ресурса)\n" +
                      "        3. run - Попытка запуска ресурса)\n",
        fullName = "action"
    ).required()

    val resource by parser.option(
        ArgType.String,
        description = "Логин пользователя",
        fullName = "resource"
    ).required()

    val volume by parser.option(
        ArgType.String,
        description = "Логин пользователя",
        fullName = "volume"
    ).required()

    parser.parse(args)

    return Arguments(login, password, action, resource, volume.toInt())
}

data class Arguments(val login: String, val password: String,
                     val action: String, val resource: String,
                     val volume: Int)