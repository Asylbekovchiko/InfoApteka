package kg.sunrise.infoapteka.networking.models.request

import com.google.gson.annotations.SerializedName

data class CustomerRequest(

    @SerializedName("city")
    val city: Int,

    @SerializedName("birth_date")
    val birthDate: String,

    @SerializedName("last_name")
    val lastName: String,

    @SerializedName("middle_name")
    val middleName: String? = null,

    @SerializedName("first_name")
    val firstName: String
)