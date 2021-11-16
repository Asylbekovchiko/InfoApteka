package kg.sunrise.infoapteka.networking.models.response

import com.google.gson.annotations.SerializedName

data class InfoResponse(
    @SerializedName("pdf_file")
    val pdfFile: String?
)
