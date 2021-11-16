package kg.sunrise.infoapteka.ui.main.menu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.fragment.BaseFragmentWithVM
import kg.sunrise.infoapteka.databinding.FragmentMenuBinding
import kg.sunrise.infoapteka.enums.UserType
import kg.sunrise.infoapteka.networking.models.response.SellerProfileResponse
import kg.sunrise.infoapteka.ui.main.menu.adapter.MenuAdapter
import kg.sunrise.infoapteka.ui.main.menu.adapter.MenuAdapterDelegate
import kg.sunrise.infoapteka.ui.main.menu.adapter.MenuType
import kg.sunrise.infoapteka.ui.main.menu.profile.bottomSheet.ExitBottomSheetFragment
import kg.sunrise.infoapteka.utils.extensions.gone
import kg.sunrise.infoapteka.utils.extensions.navigate
import kg.sunrise.infoapteka.utils.extensions.navigateToAuth
import kg.sunrise.infoapteka.utils.preference.getUserType
import kg.sunrise.infoapteka.utils.preference.isAuthorized
import org.koin.androidx.viewmodel.ext.android.viewModel


class MenuFragment : BaseFragmentWithVM<FragmentMenuBinding, MenuViewModel>(),
    MenuAdapterDelegate {

    override val viewModel: MenuViewModel by viewModel()
    override val progressBar: ConstraintLayout by lazy {
        binding.inclProgress.clProgress
    }

    override var isToShowProgress: Boolean = false

    private var menuAdapter = MenuAdapter(this)

    override fun makeRequests() {
        if (isAuthorized(requireContext()) && (getUserType(requireContext()) == UserType.SELLER)) {
            viewModel.getSellerProfile()
        }
    }

    override fun findTypeOfObject(data: Any?) {
        if (data is SellerProfileResponse) {
            menuAdapter.fillModerationDescription(data.status)
        }
    }

    override fun successRequest() {
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): FragmentMenuBinding {
        return FragmentMenuBinding.inflate(inflater)
    }

    override fun init() {
        setupToolbar()
        setupAdapter()
    }

    private fun setupToolbar() {
        binding.inclToolbar.tvToolbar.setText(R.string.Menu)
        binding.inclToolbar.ivBack.gone()
    }

    private fun setupAdapter() {
        val menuItems =
            viewModel.getMenuDTO(getUserType(requireContext()))
        menuAdapter.setData(menuItems)
        binding.rvMenuItems.adapter = menuAdapter
    }

    override fun onItemClickListener(type: MenuType) {
        when (type) {
            MenuType.RULES -> navigateToRules()
            MenuType.HELP -> navigateToHelp()
            MenuType.ABOUT_COMPANY -> navigateToCompanyInfo()
            MenuType.MY_PRODUCTS -> navigateToMyProducts()
            MenuType.EXIT -> handleExit()
            MenuType.ORDERS_HISTORY -> navigateToOrdersHistory()
            MenuType.PROFILE -> navigateToProfile()
            MenuType.ENTER -> requireActivity().navigateToAuth()
        }
    }

    private fun navigateToProfile() {
        val action = when (getUserType(requireContext())) {
            UserType.CLIENT -> MenuFragmentDirections.actionMenuFragmentToCustomerProfileFragment()
            UserType.SELLER -> MenuFragmentDirections.actionMenuFragmentToSellerProfileFragment()
            UserType.UNAUTHORIZED -> null
        }
        action?.let { navigate(action) }
    }

    private fun navigateToOrdersHistory() {
        val action = MenuFragmentDirections.actionMenuFragmentToOrdersHistoryFragment()
        findNavController().navigate(action)
    }

    private fun handleExit() {
        ExitBottomSheetFragment().show(parentFragmentManager, null)
    }

    private fun navigateToMyProducts() {
        val action = MenuFragmentDirections.actionMenuFragmentToMyProductsFragment()
        findNavController().navigate(action)
    }

    private fun navigateToCompanyInfo() {
        val action = MenuFragmentDirections.actionMenuFragmentToAboutCompanyFragment2()
        findNavController().navigate(action)
    }

    private fun navigateToHelp() {
        val action = MenuFragmentDirections.actionMenuFragmentToHelpFragment()
        findNavController().navigate(action)
    }

    private fun navigateToRules() {
        val action = MenuFragmentDirections.actionMenuFragmentToPrivacyPolicyFragment()
        findNavController().navigate(action)
    }
}