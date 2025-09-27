import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.required
import java.io.PrintStream
import java.nio.charset.StandardCharsets

fun main(args: Array<String>) {
    System.setOut(PrintStream(System.out, true, StandardCharsets.UTF_8))

    val arguments = parseArguments(args)

    println("Программа продолжает работу с кодом: " + ResponseCode.GET_REPORT.value)

    val getUser: User? = getUserHashPassword(arguments.login)

    if (getUser == null) {
        println("Программа завершила свою работу с кодом: " + ResponseCode.INCORRECT_LOGIN.value)
        return
    }

    if (getUser.password != hashPassword(arguments.password)) {
        println("Программа завершила свою работу с кодом: " + ResponseCode.INCORRECT_PASSWORD.value)
        return
    }

    val findResource: Resource? = findResource(arguments.resource)
    if  (findResource == null) {
        println("Программа завершила свою работу с кодом: " + ResponseCode.BAD_RESOURCE.value)
        return
    }

    if (arguments.volume > 0) {
        val success = processResource(arguments.resource, arguments.volume)
        if (!success) {
            println("Программа завершила свою работу с кодом: ${ResponseCode.BIG_VALUE}")
            return
        }
    }

    try {
        Action.valueOf(arguments.action.uppercase())
    } catch (e: Exception) {
        println("Программа завершила свою работу с кодом: " + ResponseCode.BAD_ACTION.value)
        return
    }

    val userAccessValue = findResource!!.accessList.find { it.userLogin == arguments.login }

    var accessAllowed = false
    if (userAccessValue != null) {
        when (arguments.action) {
            Action.READ.value -> {
                accessAllowed = checkAcces(userAccessValue.access, 0)
            }
            Action.WRITE.value -> {
                accessAllowed = checkAcces(userAccessValue.access, 1)
            }
            Action.RUN.value -> {
                accessAllowed = checkAcces(userAccessValue.access, 2)
            }
        }

        if (!accessAllowed) {
            println("Программа завершила свою работу с кодом: " + ResponseCode.NOT_ACCESS.value)
            return
        }
    }

    println("Программа завершила свою работу с кодом: " + ResponseCode.SUCCES.value)
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