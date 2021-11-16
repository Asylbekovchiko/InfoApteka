package kg.sunrise.infoapteka.networking.models.response

import com.google.gson.annotations.SerializedName

data class CheckUserResponse(
    @SerializedName("exists")
    val exists: Boolean,
    @SerializedName("message")
    val message: String?
)