package kg.sunrise.infoapteka.ui.shared.searchFragment.searchResult

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.fragment.BaseFragmentWithPaging
import kg.sunrise.infoapteka.databinding.FragmentSearchResultBinding
import kg.sunrise.infoapteka.enums.ProductShowType
import kg.sunrise.infoapteka.enums.SortType
import kg.sunrise.infoapteka.networking.models.response.ProductResponse
import kg.sunrise.infoapteka.networking.models.response.ProductsPagingResponse
import kg.sunrise.infoapteka.ui.main.AuthBotomSheet
import kg.sunrise.infoapteka.ui.main.MainActivity
import kg.sunrise.infoapteka.ui.main.home.HomeFragmentDirections
import kg.sunrise.infoapteka.ui.shared.preorderFragment.PreorderBottomSheet
import kg.sunrise.infoapteka.ui.shared.productPaging.ProductAdapterDelegate
import kg.sunrise.infoapteka.ui.shared.productPaging.ProductPagingAdapter
import kg.sunrise.infoapteka.ui.shared.searchFragment.searchResult.sortBottomSheet.SortBottomSheetFragment
import kg.sunrise.infoapteka.ui.shared.searchFragment.searchResult.sortBottomSheet.adapter.SortAdapterDelegate
import kg.sunrise.infoapteka.utils.constants.defaultIntValue
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

class SearchResultFragment
    : BaseFragmentWithPaging<ProductResponse, FragmentSearchResultBinding, SearchResultViewModel>(),
    ProductAdapterDelegate, SortAdapterDelegate {

    override val viewModel: SearchResultViewModel by viewModel()
    override val progressBar: ConstraintLayout by lazy {
        binding.inclProgress.clProgress
    }

    val args by navArgs<SearchResultFragmentArgs>()

    override var isToShowProgress: Boolean = true

    private lateinit var productAdapter: ProductPagingAdapter
    private lateinit var layoutManager: StaggeredGridLayoutManager

    private var sortSheetFragment: SortBottomSheetFragment? = null

    override fun makeRequests() {
        viewModel.clearDataIfNeed()
        getProducts(categoryID = viewModel.productCategoryID, search = viewModel.productName)
    }

    override fun findTypeOfObject(data: Any?) {
        if (data is ProductsPagingResponse) {
            binding.tvSearchCount.text = getString(R.string.Search_results_count, data.count)
        }
    }

    override fun successRequest() {
    }

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchResultBinding {
        return FragmentSearchResultBinding.inflate(inflater)
    }

    override fun init() {
        setupProductAdapter()
        initListeners()
        setupUI()
    }

    private fun setupUI() {
        binding.inclSort.ivGridStyle.setImageResource(R.drawable.ic_card_grid_selected)

        viewModel.productCategoryID = if (args.categoryID == defaultIntValue) null else args.categoryID
        viewModel.productName = args.search

        if (args.categoryID > 0) {
            binding.tvSearchCount.gone()
//            binding.tvSearchCategory.text = args.categoryName
        }
    }

    private fun navigateToSearchHint() {
        val action = HomeFragmentDirections.actionGlobalSearchHingFragment()
        findNavController().navigate(action)
    }

    private fun initListeners() {
        binding.apply {
            inclToolbar.apply {
                ivLogo.gone()
                ivArrowBack.visible()
                ivArrowBack.setOnClickListener { findNavController().navigateUp() }

                ivNotification.setOnClickListener {
                    if (isAuthorized(requireContext()))
                        findNavController().navigate(R.id.notificationFragment)
                    else {
                        AuthBotomSheet().show(parentFragmentManager, null)
                    }
                }

                btnSearch.setOnClickListener {
                    navigateToSearchHint()
                }
            }

            inclSort.apply {
                cardGridStyle.setOnClickListener {
                    viewModel.spanCount = 2
                    layoutManager?.spanCount = viewModel.spanCount
                    productAdapter.updateShowType(ProductShowType.GRID)
                    productAdapter.notifyItemRangeChanged(0, productAdapter.itemCount)
                    binding.inclSort.ivGridStyle.setImageResource(R.drawable.ic_card_grid_selected)
                    binding.inclSort.ivBlockStyle.setImageResource(R.drawable.ic_card_block_unchecked)
                }

                cardBlockStyle.setOnClickListener {
                    viewModel.spanCount = 1
                    layoutManager?.spanCount = viewModel.spanCount
                    productAdapter.updateShowType(ProductShowType.BLOCK)
                    productAdapter.notifyItemRangeChanged(0, productAdapter.itemCount)
                    binding.inclSort.ivGridStyle.setImageResource(R.drawable.ic_card_grid_unchecked)
                    binding.inclSort.ivBlockStyle.setImageResource(R.drawable.ic_card_block_selected)
                }

                btnSort.setOnClickListener {
                    sortSheetFragment = SortBottomSheetFragment(
                        this@SearchResultFragment,
                        viewModel.sortItems
                    )

                    sortSheetFragment?.show(childFragmentManager, null)
                }
            }
        }
    }

    private fun setupProductAdapter() {
        productAdapter = ProductPagingAdapter(this@SearchResultFragment)
        layoutManager = StaggeredGridLayoutManager(viewModel.spanCount, StaggeredGridLayoutManager.VERTICAL)
        binding.rvItems.layoutManager = layoutManager

        binding.rvItems.adapter = productAdapter
        lifecycleScope.launch {
            productAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect()
        }

        productAdapter.addLoadStateListener {
            val isItemsEmpty = it.refresh is LoadState.NotLoading && productAdapter.itemCount == 0

            try {
                if (isItemsEmpty) {
                    binding.clEmpty.visible()
                    binding.rvItems.gone()
                } else {
                    binding.clEmpty.gone()
                    binding.rvItems.visible()
                }
            } catch (e: Throwable) {
                Timber.e(e)
            }
        }
    }

    private fun getProducts(filter: String? = null, categoryID: Int? = null, ordering: String? = null, search: String? = null, ownerId: Int? = null) {
        requestJob = lifecycleScope.launch {
            viewModel.getProductPaging(filter, categoryID, ordering, search, ownerId).collectLatest {
                productAdapter.submitData(it)
            }
        }
    }

    // delegate implementation
    override fun onProductClick(position: Int) {
        val item = productAdapter.getItemAtPosition(position)

        item?.let {
            navigateToProductDetail(it.id)
        }
        view?.let { snackbarPosition(it, position) }
    }

    override fun onFavoriteClick(position: Int) {
        val item = productAdapter.getItemAtPosition(position)
        if (isAuthorized(requireContext())) {
            item?.let {
                it.isFavourite = !it.isFavourite
                productAdapter.notifyItemChanged(position)

                isToShowProgress = false
                viewModel.addRemoveFavourite(it.isFavourite, it.id)
            }
        } else {
            AuthBotomSheet().show(parentFragmentManager, null)
        }
    }

    override fun onBasketClick(position: Int) {
        val item = productAdapter.getItemAtPosition(position)
        if (isAuthorized(requireContext())) {
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
        } else {
            AuthBotomSheet().show(parentFragmentManager, null)
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

    // Sort Delegate
    override fun onSortItemClick(type: SortType, position: Int) {
        viewModel.sortType = type
        val sortItems = viewModel.sortItems
        sortItems.forEach { it.isChecked = false }
        val item = sortItems[position]
        item.isChecked = true
        sortSheetFragment?.dismiss()
        sortSheetFragment = null

        viewModel.clearPaging()
        setupProductAdapter()

        if (viewModel.sortType == SortType.BESTSELLER) {
            getProducts(filter = SortType.BESTSELLER.ordering, categoryID = viewModel.productCategoryID)
        } else {
            getProducts(categoryID = viewModel.productCategoryID, ordering = viewModel.sortType.ordering)
        }
    }

    private fun navigateToProductDetail(productID: Int) {
        val action = SearchResultFragmentDirections.actionGlobalProductDetailFragment(productID)
        findNavController().navigate(action)
    }
}