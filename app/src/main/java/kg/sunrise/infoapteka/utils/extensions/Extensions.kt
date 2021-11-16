package kg.sunrise.infoapteka.utils.extensions

fun <T> List<T>?.toArrayList(): ArrayList<T>? =
    if (this != null) ArrayList(this)
    else null

fun <T> List<T>?.toArrayListOrEmpty(): ArrayList<T> =
    this.toArrayList() ?: ArrayList()