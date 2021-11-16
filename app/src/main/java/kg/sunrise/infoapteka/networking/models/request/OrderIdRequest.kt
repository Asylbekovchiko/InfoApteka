package kg.sunrise.infoapteka.networking.models.request

import com.google.gson.annotations.SerializedName

data class OrderIdRequest(
    @SerializedName("order_id")
    val orderID: Int
)