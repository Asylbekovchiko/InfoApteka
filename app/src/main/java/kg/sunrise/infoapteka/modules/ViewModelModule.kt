package kg.sunrise.infoapteka.modules

import kg.sunrise.infoapteka.ui.auth.AuthViewModel
import kg.sunrise.infoapteka.ui.auth.confirm.TimerViewModel
import kg.sunrise.infoapteka.ui.main.basket.seller.SellerViewModel
import kg.sunrise.infoapteka.ui.main.basket.BasketViewModel
import kg.sunrise.infoapteka.ui.main.basket.ordering.OrderingViewModel
import kg.sunrise.infoapteka.ui.main.category.CategoryViewModel
import kg.sunrise.infoapteka.ui.main.favourite.FavouriteViewModel
import kg.sunrise.infoapteka.ui.main.home.farmAdresses.fragments.farmAdresses.mainFarm.FarmAdressViewModel
import kg.sunrise.infoapteka.ui.main.home.HomeViewModel
import kg.sunrise.infoapteka.ui.main.home.farmAdresses.fragments.farmAdresses.detailFarm.FarmDetailViewModel
import kg.sunrise.infoapteka.ui.main.menu.MenuViewModel
import kg.sunrise.infoapteka.ui.main.menu.myProducts.MyProductsViewModel
import kg.sunrise.infoapteka.ui.main.menu.profile.ProfileViewModel
import kg.sunrise.infoapteka.ui.main.product.ProductViewModel
import kg.sunrise.infoapteka.ui.main.product.editProduct.EditProductViewModel
import kg.sunrise.infoapteka.ui.shared.InfoViewModel
import kg.sunrise.infoapteka.ui.shared.notificationFragment.NotificationViewModel
import kg.sunrise.infoapteka.ui.shared.searchFragment.SearchViewModel
import kg.sunrise.infoapteka.ui.shared.searchFragment.searchResult.SearchResultViewModel
import kg.sunrise.infoapteka.ui.splashScreen.preface.PrefaceViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { SearchViewModel() }
    viewModel { MenuViewModel(get()) }
    viewModel { InfoViewModel(get()) }
    viewModel { CategoryViewModel(get()) }
    viewModel { SearchResultViewModel(get(), get()) }
    viewModel { PrefaceViewModel(get()) }
    viewModel { AuthViewModel(get()) }
    viewModel { TimerViewModel() }
    viewModel { NotificationViewModel(get()) }
    viewModel { FavouriteViewModel(get(), get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { FarmAdressViewModel(get()) }
    viewModel { SellerViewModel(get(), get()) }
    viewModel { ProductViewModel(get()) }
    viewModel { EditProductViewModel(get()) }
    viewModel { MyProductsViewModel(get()) }
    viewModel { BasketViewModel(get()) }
    viewModel { FarmDetailViewModel(get()) }
    viewModel { OrderingViewModel(get()) }

}