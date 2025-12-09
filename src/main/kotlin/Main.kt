import models.ResponseCode
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import repository.sqlite.DatabaseInitializer
import services.ArgumentParser
import services.ResourceManager
import services.UserAuthentication
import java.io.PrintStream
import java.nio.charset.StandardCharsets

@SpringBootApplication(scanBasePackages = ["services", "repository", "models"])
class LabTwoApplication(
    private val argumentParser: ArgumentParser,
    private val userAuthentication: UserAuthentication,
    private val resourceManager: ResourceManager,
    private val databaseInitializer: DatabaseInitializer
) : CommandLineRunner {

    private val logger = LoggerFactory.getLogger(LabTwoApplication::class.java)

    override fun run(args: Array<String>) {
        System.setOut(PrintStream(System.out, true, StandardCharsets.UTF_8))
        databaseInitializer.ensureInitialized()

        val arguments = argumentParser.parse(args) ?: return

        val (user, userAuthResponseCode) = userAuthentication.tryGetUser(arguments.login, arguments.password)

        if (userAuthResponseCode != ResponseCode.SUCCESS) {
            println(userAuthResponseCode)
            logger.warn("Authentication failed for user {}", arguments.login)
            return
        }

        val (resource, resourceResponseCode) = resourceManager.tryGetResource(arguments.resource, arguments.volume)

        if (resourceResponseCode != ResponseCode.SUCCESS || resource == null || user == null) {
            println(resourceResponseCode)
            logger.warn("Resource access failed for {}: {}", arguments.resource, resourceResponseCode)
            return
        }

        val result = resourceManager.tryDoAction(resource, user, arguments.action, arguments.volume)
        println(result.value)
        logger.info(
            "User {} executed {} on {} with volume {} -> {}",
            user.login, arguments.action, arguments.resource, arguments.volume, result
        )
    }
}

fun main(args: Array<String>) {
    runApplication<LabTwoApplication>(*args)
}