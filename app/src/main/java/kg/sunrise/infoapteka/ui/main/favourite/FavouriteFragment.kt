package kg.sunrise.infoapteka.ui.main.favourite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.google.android.material.snackbar.Snackbar
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.fragment.BaseFragmentWithPaging
import kg.sunrise.infoapteka.databinding.FragmentFavouriteBinding
import kg.sunrise.infoapteka.ui.main.MainActivity
import kg.sunrise.infoapteka.ui.shared.productPaging.ProductPagingAdapter
import kg.sunrise.infoapteka.ui.shared.productPaging.ProductAdapterDelegate
import kg.sunrise.infoapteka.networking.models.response.ProductResponse
import kg.sunrise.infoapteka.ui.shared.productDetailFragment.ProductDetailFragmentDirections
import kg.sunrise.infoapteka.ui.main.AuthBotomSheet
import kg.sunrise.infoapteka.ui.shared.preorderFragment.PreorderBottomSheet
import kg.sunrise.infoapteka.utils.constants.SUCCESS_REQUEST
import kg.sunrise.infoapteka.utils.extensions.gone
import kg.sunrise.infoapteka.utils.extensions.snackbarPosition
import kg.sunrise.infoapteka.utils.extensions.visible
import kg.sunrise.infoapteka.utils.preference.isAuthorized
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class FavouriteFragment
    : BaseFragmentWithPaging<ProductResponse, FragmentFavouriteBinding, FavouriteViewModel>(),
    ProductAdapterDelegate {

    override val viewModel: FavouriteViewModel by viewModel()
    override val progressBar: ConstraintLayout by lazy {
        binding.inclProgress.clProgress
    }

    override var isToShowProgress: Boolean = true

    private lateinit var productAdapter: ProductPagingAdapter

    override fun makeRequests() {
        viewModel.clearDataIfNeed()

        getFavoriteProducts()
    }

    override fun init() {
        setupProductAdapter()
        initListeners()
        setupUI()
    }

    private fun initListeners() {
        binding.swipeRefresh.setOnRefreshListener {
            swipeRefresh()
        }
    }

    private fun swipeRefresh() {
        binding.swipeRefresh.isRefreshing = false

        viewModel.clearPaging()

        makeRequests()
    }

    private fun setupUI() {
        binding.inclToolbar.tvToolbar.setText(R.string.Favourites)
        binding.inclToolbar.ivBack.gone()
    }

    private fun setupProductAdapter() {
        productAdapter = ProductPagingAdapter(this)

        binding.rvItems.adapter = productAdapter
        lifecycleScope.launch {
            productAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect()
        }

        productAdapter.addLoadStateListener { loadState ->
            val isListEmpty = loadState.refresh is LoadState.NotLoading && productAdapter.itemCount == 0

            try {
                if (isListEmpty) {
                    binding.clFavoriteEmpty.visible()
                    binding.rvItems.gone()
                } else {
                    binding.clFavoriteEmpty.gone()
                    binding.rvItems.visible()
                }
            } catch (e: Throwable) {
                Timber.e(e)
            }
        }
    }

    private fun getFavoriteProducts() {
        requestJob = lifecycleScope.launch {
            viewModel.getFavoriteProductPaging().collectLatest {
                productAdapter.submitData(it)
            }
        }
    }

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavouriteBinding {
        return FragmentFavouriteBinding.inflate(inflater)
    }

    override fun findTypeOfObject(data: Any?) {
        if (data == SUCCESS_REQUEST) {
            viewModel.clearPaging()
            getFavoriteProducts()
        }
    }

    override fun successRequest() {
    }

    // delegate implementation
    override fun onProductClick(position: Int) {
        val item = productAdapter.getItemAtPosition(position)
        item?.let {
            navigateToProductDetail(it.id)
        }
        view?.let { snackbarPosition(it, position, Snackbar.LENGTH_SHORT) }
    }

    override fun onFavoriteClick(position: Int) {
        val item = productAdapter.getItemAtPosition(position)
        item?.let {
            it.isFavourite = !it.isFavourite
            productAdapter.notifyItemChanged(position)

            isToShowProgress = false
            viewModel.addRemoveFavourite(it.isFavourite, it.id)
        }
    }

    override fun onBasketClick(position: Int) {
        val item = productAdapter.getItemAtPosition(position)
        item?.let {

            it.isInBasket = !it.isInBasket
            productAdapter.notifyItemChanged(position)

            isToShowProgress = false
            viewModel.addRemoveBasket(it.isInBasket, it.id)

            if (it.isInBasket) {
                val activity = (requireActivity() as? MainActivity)
                activity?.let { mainActivity ->
                    val badge = mainActivity.binding.bottomNav.getOrCreateBadge(R.id.basket_nav_graph)
                    badge.isVisible = true


                    mainActivity.showAddItemSnackbar()
                }
            }
        }
    }

    override fun onIsNotAvailableClick(response: ProductResponse) {
        if (isAuthorized(requireContext())) {
            if (response.owner.address != null && response.owner.phone != null) {
                val bottomSheet = PreorderBottomSheet(
                    response.owner.firstName,
                    response.owner.address,
                    response.owner.phone)
                bottomSheet.show(childFragmentManager, null)
            }
        } else {
            AuthBotomSheet().show(parentFragmentManager, null)
        }
    }

    private fun navigateToProductDetail(productID: Int) {
        val action = FavouriteFragmentDirections.actionGlobalProductDetailFragment(productID)
        findNavController().navigate(action)
    }
}