package kg.sunrise.infoapteka.networking.models.response

import com.google.gson.annotations.SerializedName


data class BranchAdressesPaging(
    val count: Int, val next: Int?, val previous: Int?,
    val results: ArrayList<BranchAdressesResponse>
)

data class BranchAdressesResponse(
    val id: Int,
    val image: String,
    val name: String,
    val address: String,
    @SerializedName("is_open") var isOpen: Boolean,
    val distance: String,
    val latitude: Double,
    val longitude: Double
)

data class BranchResponseDetail( val id: Int,
                                 val images: ArrayList<String>?,
                                 val name: String,
                                 val address: String,
                                 val phones : ArrayList<String> ,
                                 @SerializedName("is_open") var isOpen: Boolean,
                                 @SerializedName("working_hours")
                                 val isWorking : String ,
                                 val distance: String,
                                 val latitude: Double,
                                 val longitude: Double)