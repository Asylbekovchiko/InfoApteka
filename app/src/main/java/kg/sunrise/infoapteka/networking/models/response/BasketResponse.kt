package kg.sunrise.infoapteka.networking.models.response

import com.google.gson.annotations.SerializedName

data class BasketResponse(val id: Int,
                          @SerializedName("cart_items") val cartItems: ArrayList<CartItem>,
                          @SerializedName("get_cart_total_items") val countOfItems: Int,
                          @SerializedName("get_cart_total") val getTotalValue: String)


data class CartItem(val id: Int,
                    val quantity: Int,
                    val drug: ProductResponse)


data class Drug(val id: Int,
                val name: String,
                val price: String,
                val image: String,
                @SerializedName("in_favorites") val inFavourites: String,
                val owner : String ,
                @SerializedName("in_card") val inCart: String,
                @SerializedName("is_available") val isAvailable: Boolean,
                @SerializedName("is_owner") val isOwner: String)