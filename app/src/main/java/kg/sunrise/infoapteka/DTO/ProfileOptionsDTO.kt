package kg.sunrise.infoapteka.ui.main.menu.profile.sellerProfile

import kg.sunrise.infoapteka.ui.main.menu.profile.adapter.MenuProfileOption

data class ProfileOptionsDTO(
    //val profileItem: ArrayList<ProfileItem>,
    val profileWithIv: ArrayList<MenuProfileOption>
)


data class ProfileItem(val title: String, val desc: String)