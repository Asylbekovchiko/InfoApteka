package kg.sunrise.infoapteka.networking.models.request

import com.google.gson.annotations.SerializedName

data class UserRequest(
    @SerializedName("phone")
    val phoneNumber: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("middle_name")
    val middleName: String?,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("city")
    val city: Int,
    @SerializedName("address")
    val address: String?,
    @SerializedName("birth_date")
    val birthday: String
)
