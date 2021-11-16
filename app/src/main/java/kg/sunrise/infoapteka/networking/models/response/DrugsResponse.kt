package kg.sunrise.infoapteka.networking.models.response

import com.google.gson.annotations.SerializedName
import kg.sunrise.infoapteka.enums.ModerationType
import kg.sunrise.infoapteka.ui.main.menu.myProducts.adapter.MyProductDTO
import kg.sunrise.infoapteka.utils.constants.APPROVED
import kg.sunrise.infoapteka.utils.constants.ON_MODERATION
import kg.sunrise.infoapteka.utils.constants.REJECTED

data class DrugPagingResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: Int?,
    @SerializedName("previous")
    val previous: Int?,
    @SerializedName("results")
    val results: ArrayList<MyDrug>
)

data class MyDrug(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("price")
    val price: Int,
    @SerializedName("image")
    val image: String?,
    @SerializedName("owner")
    val owner: String,
    @SerializedName("status")
    val status: String
)

fun MyDrug.mapToMyProductDTO() = MyProductDTO(
    id,
    name,
    owner,
    image,
    price.toString(),
    getModerationType(status)
)

private fun getModerationType(status: String): ModerationType =
    when (status) {
        REJECTED -> ModerationType.DISMISSED
        ON_MODERATION -> ModerationType.IN_PROGRESS
        APPROVED -> ModerationType.ALLOWED
        else -> ModerationType.EMPTY
    }