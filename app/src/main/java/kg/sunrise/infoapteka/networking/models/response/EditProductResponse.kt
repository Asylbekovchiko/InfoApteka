package kg.sunrise.infoapteka.networking.models.response

import com.google.gson.annotations.SerializedName

data class EditProductResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("category")
    val category: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("instruction_txt")
    val instructionTxt: String,
    @SerializedName("price")
    val price: Int,
    @SerializedName("images")
    val images: ArrayList<Int> = ArrayList()
)
