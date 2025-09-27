import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.required

fun main(args: Array<String>) {
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

    if(!findResource(arguments.resource)) {
        println("Программа завершила свою работу с кодом: " + ResponseCode.BAD_RESOURCE.value)
    }

    try {
        Action.valueOf(arguments.action)
    } catch (e: Exception) {
        println("Программа завершила свою работу с кодом: " + ResponseCode.BAD_ACTION.value)
    }

    println("Программа завершила свою работу с кодом: " + ResponseCode.SUCCES.value)
}


fun parseArguments(args: Array<String>): Arguments {
    val parser = ArgParser("LabTwo")

    val login by parser.option(
        ArgType.String,
        fullName = "login"
    ).required()

    val password by parser.option(
        ArgType.String,
        fullName = "password"
    ).required()

    val action by parser.option(
        ArgType.String,
        fullName = "action"
    ).required()

    val resource by parser.option(
        ArgType.String,
        fullName = "resource"
    ).required()

    val volume by parser.option(
        ArgType.String,
        fullName = "volume"
    ).required()

    parser.parse(args)

    return Arguments(login, password, action, resource, volume)
}

data class Arguments(val login: String, val password: String,
                     val action: String, val resource: String,
                     val volume: String)