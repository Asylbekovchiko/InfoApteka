package kg.sunrise.infoapteka.networking.models.response

import com.google.gson.annotations.SerializedName

data class SertficateResponse(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("image") val image: String
)