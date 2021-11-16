package kg.sunrise.infoapteka.ui.main.menu.profile.adapter

import kg.sunrise.infoapteka.ui.shared.productPaging.ProductAdapterDelegate
import kg.sunrise.infoapteka.utils.enums.ProfileInfoType

class ProfileAdapter(val delegate: ProfileDateDelegate) {
}

interface ProfileDateDelegate {

    fun updateDateClick(date: String)
}

interface ProfileInfo{
    fun updateProfileInfo(info: String, profileType: ProfileInfoType)
    fun onItemProfileClick(profileType: ProfileInfoType)
}


interface ProfileAdapterDelegate: ProductAdapterDelegate {

    override fun onProductClick(position: Int) {

    }

    override fun onFavoriteClick(position: Int) {
    }

    override fun onBasketClick(position: Int) {
    }
}