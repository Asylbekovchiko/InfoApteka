package kg.sunrise.infoapteka.networking.models.response

import com.google.gson.annotations.SerializedName

data class PrivacyPolicyResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("pdf_file")
    val pdfFile: String
)
