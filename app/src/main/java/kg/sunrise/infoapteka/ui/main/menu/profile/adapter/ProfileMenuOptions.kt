package kg.sunrise.infoapteka.ui.main.menu.profile.adapter

import kg.sunrise.infoapteka.utils.enums.ProfileInfoType

sealed class MenuProfileOption(
    val title: String,
    val description: String,
    val type: ProfileInfoType
) {
    class ProfileMainItem(title: String, description: String, type: ProfileInfoType) 
        : MenuProfileOption(title, description, type)

    class ProfileCityItem(title: String, description: String, type: ProfileInfoType) 
        : MenuProfileOption(title, description, type)

    class ProfileBirthDate(title: String, description: String, type: ProfileInfoType)
        : MenuProfileOption(title, description, type)

    class ProfileNumber(title: String, description: String, type: ProfileInfoType)
        : MenuProfileOption(title, description, type)

}