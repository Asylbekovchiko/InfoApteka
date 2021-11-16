package kg.sunrise.infoapteka.networking.models.response

import com.google.gson.annotations.SerializedName
import kg.sunrise.infoapteka.ui.main.product.PhotoUrl

data class PhotoResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String
)

fun PhotoResponse.mapToPhotoUrl() = PhotoUrl(
    id, image, image.substringAfterLast("/")
)