package kg.sunrise.infoapteka.networking.models.response

import com.google.gson.annotations.SerializedName


data class SellerProfileResponse(
    val address: String,
    val avatar: String,
    @SerializedName("birth_date")
    val birthDate: String ,
    val certificates: ArrayList<SertficateResponse>,
    val city: CityItem,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("middle_name")
    val middleName: String,
    val phone: String,
    val status: String
)