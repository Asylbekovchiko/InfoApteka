package kg.sunrise.infoapteka.ui.main.basket.seller

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.fragment.BaseFragmentWithPaging
import kg.sunrise.infoapteka.databinding.FragmentSellerBasketBinding
import kg.sunrise.infoapteka.enums.ProductShowType
import kg.sunrise.infoapteka.enums.SortType
import kg.sunrise.infoapteka.networking.models.response.ProductResponse
import kg.sunrise.infoapteka.networking.models.response.SellerInfoResponse
import kg.sunrise.infoapteka.ui.main.AuthBotomSheet
import kg.sunrise.infoapteka.ui.main.MainActivity
import kg.sunrise.infoapteka.ui.shared.preorderFragment.PreorderBottomSheet
import kg.sunrise.infoapteka.ui.shared.productDetailFragment.ProductDetailFragmentDirections
import kg.sunrise.infoapteka.ui.shared.productPaging.ProductAdapterDelegate
import kg.sunrise.infoapteka.ui.shared.productPaging.ProductPagingAdapter
import kg.sunrise.infoapteka.ui.shared.searchFragment.searchResult.sortBottomSheet.SortBottomSheetFragment
import kg.sunrise.infoapteka.ui.shared.searchFragment.searchResult.sortBottomSheet.adapter.SortAdapterDelegate
import kg.sunrise.infoapteka.utils.extensions.snackbarPosition
import kg.sunrise.infoapteka.utils.preference.isAuthorized
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SellerProductsFragment :
    BaseFragmentWithPaging<ProductResponse, FragmentSellerBasketBinding, SellerViewModel>(),
    ProductAdapterDelegate, SortAdapterDelegate {

    override val viewModel: SellerViewModel by viewModel()

    override val progressBar: ConstraintLayout by lazy {
        binding.inclProgress.clProgress
    }

    override var isToShowProgress: Boolean = true

    private lateinit var productAdapter: ProductPagingAdapter
    private lateinit var layoutManager: StaggeredGridLayoutManager

    private var sortSheetFragment: SortBottomSheetFragment? = null

    override fun makeRequests() {
        viewModel.getSellerInfo(navArgs.ownerId)
        viewModel.clearDataIfNeed()
        getProducts(categoryId = viewModel.productCategoryID, search = viewModel.productName, ownerId =navArgs.ownerId)
    }

    private val navArgs by navArgs<SellerProductsFragmentArgs>()

    override fun findTypeOfObject(data: Any?) {
        when (data) {
            is SellerInfoResponse -> {
                initSellerInfo(data)
            }
        }
    }

    override fun successRequest() {
    }

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSellerBasketBinding {
        return FragmentSellerBasketBinding.inflate(layoutInflater)
    }

    override fun init() {
        setUpProductAdapter()
        setUpUI()
        initListeners()
    }

    fun initListeners() {
        binding.apply {
            inclToolbar.apply {
                ivBack.setOnClickListener { findNavController().navigateUp() }
            }

            inclSort.apply {
                cardGridStyle.setOnClickListener {
                    viewModel.spanCount = 2
                    layoutManager.spanCount = viewModel.spanCount
                    productAdapter.updateShowType(ProductShowType.GRID)
                    productAdapter.notifyItemRangeChanged(0, productAdapter.itemCount)
                    inclSort.ivGridStyle.setImageResource(R.drawable.ic_card_grid_selected)
                    inclSort.ivBlockStyle.setImageResource(R.drawable.ic_card_block_unchecked)
                }

                cardBlockStyle.setOnClickListener {
                    viewModel.spanCount = 1
                    layoutManager.spanCount = viewModel.spanCount
                    productAdapter.updateShowType(ProductShowType.BLOCK)
                    productAdapter.notifyItemRangeChanged(0, productAdapter.itemCount)
                    inclSort.ivGridStyle.setImageResource(R.drawable.ic_card_grid_unchecked)
                    inclSort.ivBlockStyle.setImageResource(R.drawable.ic_card_block_selected)
                }

                btnSort.setOnClickListener {
                    sortSheetFragment = SortBottomSheetFragment(
                        this@SellerProductsFragment,
                        viewModel.sort
                    )
                    sortSheetFragment?.show(childFragmentManager, null)
                }
            }
        }
    }

    fun setUpUI() {
        binding.inclSort.ivGridStyle.setImageResource(R.drawable.ic_card_grid_selected)
    }

    fun initSellerInfo(seller: SellerInfoResponse) {
        Glide.with(requireContext()).load(seller.avatar).placeholder(R.drawable.ic_item_placeholder).into(binding.inclToolbar.sellerLogo)
        binding.inclToolbar.tvSellerName.text = seller.firstName
        binding.inclToolbar.tvNumber.text = seller.phone
        binding.inclToolbar.tvAddress.text = seller.address
        binding.inclToolbar.tvToolbar.text = seller.firstName
    }

    fun setUpProductAdapter() {
        productAdapter = ProductPagingAdapter(this)
        layoutManager =
            StaggeredGridLayoutManager(viewModel.spanCount, StaggeredGridLayoutManager.VERTICAL)
        binding.rvProducts.layoutManager = layoutManager

        binding.rvProducts.adapter = productAdapter
        lifecycleScope.launch {
            productAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect()
        }
    }

    private fun getProducts(
        filter: String? = null,
        categoryId: Int? = null,
        ordering: String? = null,
        search: String? = null,
        ownerId: Int? = null
    ) {
        requestJob = lifecycleScope.launch {
            viewModel.getProductByPaging(filter, categoryId, ordering, search, ownerId).collectLatest {
                productAdapter.submitData(it)
            }
        }
    }

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
                viewModel.addAndDeleteFromFavorites(it.isFavourite, it.id)
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
                viewModel.addAndDeleteFromBasket(it.isInBasket, it.id)

                if (it.isInBasket) {
                    val activity = (requireActivity() as? MainActivity)
                    activity?.let {
                        val badge = activity.binding.bottomNav.getOrCreateBadge(R.id.basket_nav_graph)
                        badge.isVisible = true
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

    override fun onSortItemClick(type: SortType, position: Int) {
        viewModel.sortType = type
        val sortItems = viewModel.sort
        sortItems.forEach { it.isChecked = false }
        val item = sortItems[position]
        item.isChecked = true
        sortSheetFragment?.dismiss()
        sortSheetFragment = null

        viewModel.clearPaging()
        setUpProductAdapter()

        if (viewModel.sortType == SortType.BESTSELLER) {
            getProducts(filter = SortType.BESTSELLER.ordering, viewModel.productCategoryID, ownerId = navArgs.ownerId)
        } else {
            getProducts(categoryId = viewModel.productCategoryID, ordering = viewModel.sortType.ordering, ownerId = navArgs.ownerId)
        }
    }

    private fun navigateToProductDetail(productId: Int){
        val action = ProductDetailFragmentDirections.actionGlobalProductDetailFragment(productId)
        findNavController().navigate(action)
    }
}