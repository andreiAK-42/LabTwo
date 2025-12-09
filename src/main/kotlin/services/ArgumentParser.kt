package services

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.required
import org.springframework.stereotype.Component

private const val loginDescription = "Логин пользователя"

@Component
class ArgumentParser {
    fun parse(args: Array<String>): Arguments? {
        val parser = ArgParser("LabTwo")

        val login by parser.option(
            ArgType.String,
            description = loginDescription,
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
            description = loginDescription,
            fullName = "resource"
        ).required()

        val volume by parser.option(
            ArgType.String,
            description = loginDescription,
            fullName = "volume"
        ).required()

        return try {
            parser.parse(args)
            Arguments(login, password, action, resource, volume.toInt())
        } catch (ex: Exception) {
            null
        }
    }
}

data class Arguments(
    val login: String,
    val password: String,
    val action: String,
    val resource: String,
    val volume: Int
)