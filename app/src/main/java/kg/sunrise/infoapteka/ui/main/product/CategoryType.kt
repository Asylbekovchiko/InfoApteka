package kg.sunrise.infoapteka.ui.main.product

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class CategoryType : Parcelable

@Parcelize
object AddProductCategoryType : CategoryType()

@Parcelize
object EditProductCategoryType : CategoryType()
