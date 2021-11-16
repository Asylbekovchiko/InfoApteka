package kg.sunrise.infoapteka.networking.models.request

import com.google.gson.annotations.SerializedName

data class ProductRequest(
    @SerializedName("category")
    val category: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("instruction_txt")
    val instructionTxt: String?,
    @SerializedName("price")
    val price: Int?,
    @SerializedName("images")
    val images: ArrayList<Int>? = null
)