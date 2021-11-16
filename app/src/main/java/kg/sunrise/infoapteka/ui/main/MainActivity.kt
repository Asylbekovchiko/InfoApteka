package kg.sunrise.infoapteka.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.activity.BaseActivity
import kg.sunrise.infoapteka.databinding.ActivityMainBinding
import kg.sunrise.infoapteka.ui.auth.AuthViewModel
import kg.sunrise.infoapteka.ui.main.category.CategoryViewModel
import kg.sunrise.infoapteka.utils.extensions.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override val internetConnectionLayout : ConstraintLayout by lazy {
        binding.inclNoInternet.clNoInternet
    }

    override val serviceUnAvailableLayout: ConstraintLayout
        get() = binding.inclServiceUnavailable.clServiceUnavailable

    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding = { it ->
        ActivityMainBinding.inflate(it)
    }

    private val authVM: AuthViewModel by viewModel()

    override val navContainerId: Int = R.id.nav_host_container

    private var currentNavController: LiveData<NavController>? = null

    private val navIdsForBottomNavHide = arrayListOf<Int>(
        R.id.zoomFragment,
        R.id.ordersHistoryFragment,
        R.id.addProductFragment,
        R.id.ordersHistoryFragment,
        R.id.searchHintFragment,
        R.id.editProductFragment
    )

    private val categorySharedViewModel by viewModel<CategoryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBottomNavigationBar()
        authVM.sendDeviceTokenId(this)
    }

    private fun setupBottomNavigationBar() {
        val bottomNavigation = binding.bottomNav

        val navGraphIds = listOf(
            R.navigation.home_nav_graph,
            R.navigation.categories_nav_graph,
            R.navigation.basket_nav_graph,
            R.navigation.favourites_nav_graph,
            R.navigation.menu_nav_graph
        )

        val controller = bottomNavigation.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        ) { }

        controller.observe(this) {
            it.addOnDestinationChangedListener { controller, destination, arguments ->
                if (destination.id in navIdsForBottomNavHide) {
                    bottomNavigation.gone()
                } else {
                    bottomNavigation.visible()
                }
            }
        }

        currentNavController = controller
    }

    fun showAddItemSnackbar() {
        addProductToBasketSnackbar()
    }

    fun redirectToBasket() {
        binding.bottomNav.selectedItemId = R.id.basket_nav_graph
    }

    override fun onNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    override fun onBackPressed() {
        if (currentNavController?.value?.popBackStack() != true) {
            super.onBackPressed()
        }
    }

    private fun navigateToAuth() {
//        val intent = Intent(this, AuthActivity::class.java)
//        startActivity(intent)
//        transitionFade()
    }
}