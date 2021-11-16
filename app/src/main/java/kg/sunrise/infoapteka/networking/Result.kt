package kg.sunrise.infoapteka.networking

sealed class Result<out T>{
    data class Error(val e: String) : Result<Nothing>()
    data class Data<T>(val data: T) : Result<T>()
}