package kg.sunrise.infoapteka.enums

enum class OrderHistoryType(val value: String) {
    ACCEPTED("accepted"),
    PROCESSED("processed"),
    SENT("sent"),
    COMPLETED("completed"),
    REJECT("reject")
}