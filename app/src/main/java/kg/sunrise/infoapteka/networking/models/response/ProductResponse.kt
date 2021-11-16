package kg.sunrise.infoapteka.networking.models.response

import com.google.gson.annotations.SerializedName

data class ProductResponse(
    val id: Int,
    val name: String,
    val price: Int,
    @SerializedName("image")
    val imageURL: String?,
    @SerializedName("in_favorites")
    var isFavourite: Boolean,
    val owner: ProductOwner,
    @SerializedName("is_owner")
    val isOwner: Boolean,
    @SerializedName("in_cart")
    var isInBasket: Boolean,
    @SerializedName("is_available")
    val isAvailable: Boolean
)

data class ProductOwner(
    val id: Int,
    @SerializedName("first_name")
    val firstName: String,
    val phone: String?,
    val address: String?,
    val avatar: String?
)

data class ProductsPagingResponse(
    val count: Int,
    @SerializedName("next")
    val next: Int?,
    @SerializedName("previous")
    val previous: Int?,
    @SerializedName("results")
    val products: ArrayList<ProductResponse>
)