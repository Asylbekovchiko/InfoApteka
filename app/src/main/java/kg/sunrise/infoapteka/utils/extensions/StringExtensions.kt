package kg.sunrise.infoapteka.utils.extensions

fun String.clearPhoneNumber() =
    this.replace("[^\\d+]".toRegex(), "")