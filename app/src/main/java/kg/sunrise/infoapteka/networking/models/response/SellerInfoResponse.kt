package kg.sunrise.infoapteka.networking.models.response


import com.google.gson.annotations.SerializedName

data class SellerInfoResponse(
    @SerializedName("address")
    val address: String,
    @SerializedName("avatar")
    val avatar: Any,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("phone")
    val phone: String
)