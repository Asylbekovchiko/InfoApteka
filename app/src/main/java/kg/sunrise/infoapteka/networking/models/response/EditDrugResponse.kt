package kg.sunrise.infoapteka.networking.models.response

import com.google.gson.annotations.SerializedName
import kg.sunrise.infoapteka.ui.main.product.Category

data class EditDrugResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("category")
    val category: Category,
    @SerializedName("price")
    val price: Int,
    @SerializedName("images")
    val images: ArrayList<PhotoResponse>,
    @SerializedName("instruction_txt")
    val instructionTxt: String
)