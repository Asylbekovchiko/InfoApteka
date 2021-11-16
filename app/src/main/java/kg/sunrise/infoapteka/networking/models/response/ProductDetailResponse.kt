package kg.sunrise.infoapteka.networking.models.response

import com.google.gson.annotations.SerializedName
import kg.sunrise.infoapteka.ui.shared.productDetailFragment.adapter.Instruction

// move to dto folder
data class ProductDetailResponse(
    val id: Int,
    val images: ArrayList<BannerResponse>,
    val owner: ProductOwner,
    val price: String,
    @SerializedName("added_by_admin")
    val addedByAdmin: Boolean,
    @SerializedName("name")
    val productName: String,
    @SerializedName("in_favorites")
    var isFavorite: Boolean,
    @SerializedName("in_cart")
    var isInBasket: Boolean,
    @SerializedName("similar")
    val products: ArrayList<ProductResponse>,
    @SerializedName("is_available")
    val isAvailable: Boolean = true,
    @SerializedName("instructions")
    val instructions: ArrayList<Instruction>,
    @SerializedName("instruction_txt")
    val instructionText: String?,
    @SerializedName("is_owner")
    val isOwner: Boolean

)