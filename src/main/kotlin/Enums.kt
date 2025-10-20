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
