package kg.sunrise.infoapteka.ui.main.product

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val title: String
) : Parcelable
