package kg.sunrise.infoapteka.networking.models.response

import com.google.gson.annotations.SerializedName

data class RegistrationResponse(
    @SerializedName("token")
    val token: String,
    @SerializedName("role")
    val role: String
)
