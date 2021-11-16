package kg.sunrise.infoapteka.networking.models.response

import com.google.gson.annotations.SerializedName


data class SellerProfileUpdate(
    @SerializedName("first_name")
    val firstName: String?= null,
    @SerializedName("last_name")
    val lastName: String ?= null,
    @SerializedName("middle_name")
    val middleName: String? = null,
    val city: Int,
    val address: String?= null,
    val birth_date : String ?= null,
    val certificates: ArrayList<Int>?= null
)
