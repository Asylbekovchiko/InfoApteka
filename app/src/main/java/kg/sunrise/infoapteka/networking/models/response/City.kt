package kg.sunrise.infoapteka.networking.models.response

import com.google.gson.annotations.SerializedName

data class City(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String
)
