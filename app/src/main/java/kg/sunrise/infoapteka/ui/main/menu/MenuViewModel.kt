package kg.sunrise.infoapteka.ui.main.menu

import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.viewModel.BaseViewModel
import kg.sunrise.infoapteka.enums.ModerationType
import kg.sunrise.infoapteka.enums.UserType
import kg.sunrise.infoapteka.networking.repositories.ProfileRepository
import kg.sunrise.infoapteka.ui.main.menu.adapter.MenuOption
import kg.sunrise.infoapteka.ui.main.menu.adapter.MenuType
import kg.sunrise.infoapteka.utils.network.State

class MenuViewModel(
    val profileRepository: ProfileRepository
) : BaseViewModel() {

    fun getSellerProfile() = getViewModelScope {
        val response = profileRepository.getSellerProfile()
        if (!response.hasBody())
            return@getViewModelScope
        if (response!!.isSuccess())
            _state.value = State.SuccessState.SuccessObjectState(response.body())
    }

    fun getMenuDTO(userType: UserType): ArrayList<MenuOption> {
        val menuItems = getMenuItems(userType)

        return menuItems
    }

    private fun getMenuItems(userType: UserType): ArrayList<MenuOption> {
        var items = arrayListOf<MenuOption>()

        when (userType) {
            UserType.UNAUTHORIZED -> items.add(getUnauthorizedItems())
            UserType.CLIENT -> items += getBuyerItems()
            UserType.SELLER -> items += getSellerItems()
        }
        items += getDefaultItems()

        if (userType != UserType.UNAUTHORIZED) {
            items += MenuOption.SimpleItem(R.string.Exit_from_account, R.drawable.ic_menu_exit, MenuType.EXIT)
        }

        return items
    }

    private fun getUnauthorizedItems(): MenuOption {
        return MenuOption.SimpleItem(R.string.Enter, R.drawable.ic_menu_profile, MenuType.ENTER)
    }

    private fun getBuyerItems(): ArrayList<MenuOption> {
        return arrayListOf(
            MenuOption.ProfileItem(R.string.Profile, R.drawable.ic_menu_profile, MenuType.PROFILE, ModerationType.EMPTY),
            MenuOption.SimpleItem(R.string.Orders_history, R.drawable.ic_menu_history, MenuType.ORDERS_HISTORY)
        )
    }

    private fun getSellerItems(): ArrayList<MenuOption> {
        return arrayListOf(
            MenuOption.ProfileItem(R.string.Profile, R.drawable.ic_menu_profile, MenuType.PROFILE, ModerationType.EMPTY),
            MenuOption.SimpleItem(R.string.My_products, R.drawable.ic_menu_products, MenuType.MY_PRODUCTS),
            MenuOption.SimpleItem(R.string.Orders_history, R.drawable.ic_menu_history, MenuType.ORDERS_HISTORY)
        )
    }

    private fun getDefaultItems(): ArrayList<MenuOption> {
        return arrayListOf(
            MenuOption.SimpleItem(R.string.About_company, R.drawable.ic_menu_about_company, MenuType.ABOUT_COMPANY),
            MenuOption.SimpleItem(R.string.Help, R.drawable.ic_menu_help, MenuType.HELP),
            MenuOption.SimpleItem(R.string.Program_rules, R.drawable.ic_menu_rules, MenuType.RULES)
        )
    }
}