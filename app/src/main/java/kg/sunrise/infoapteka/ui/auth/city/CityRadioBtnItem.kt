package kg.sunrise.infoapteka.ui.auth.city

import kg.sunrise.infoapteka.networking.models.response.City

data class CityRadioBtnItem(
    val id: Int,
    val title: String,
    var isChecked: Boolean = false
)

fun City.mapToCityRadioBtnItem() =
    CityRadioBtnItem(id, title)
