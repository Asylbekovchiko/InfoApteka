package kg.sunrise.infoapteka.networking.models.response

import com.google.gson.annotations.SerializedName

data class AboutCompanyInfo(
    val id: Int,
    var title: String,
    var description: String,
    var email: String,
    var address: String,
    val latitude: Double,
    val longitude: Double,
    @SerializedName("about_socials")
    val socials: ArrayList<AboutSocial>,
    @SerializedName("phones")
    val phones: ArrayList<String>
)

data class AboutSocial(
    val logo: String?,
    val link: String,
    val name: String
)
