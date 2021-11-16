package kg.sunrise.infoapteka.networking.models.response

import com.google.gson.annotations.SerializedName
import kg.sunrise.infoapteka.ui.main.product.Category

data class CategoryItemResponse(
    val id: Int,
    val name: String,
    val logo: String?,
    @SerializedName("children")
    val items: ArrayList<CategoryItemResponse>
)

fun CategoryItemResponse.mapToCategory() = Category(
    id,
    name
)