import kotlinx.cli.*

fun main(args: Array<String>) {
    val parser = ArgParser("my-app")

    // Определяем подкоманды
    val encode by parser.subcommands(Subcommand("encode", "Encode data"))
    val decode by parser.subcommands(Subcommand("decode", "Decode data"))

    // Опции для подкоманд
    val key by parser.option(ArgType.String, shortName = "k", description = "Encryption key").required()

    parser.parse(args)

    when {
        encode.isUsed -> println("Encoding with key: $key")
        decode.isUsed -> println("Decoding with key: $key")
        else -> println("No subcommand specified")
    }
}