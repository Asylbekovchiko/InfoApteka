package kg.sunrise.infoapteka.ui.main.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.AppBarLayout
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.fragment.BaseFragmentWithPaging
import kg.sunrise.infoapteka.databinding.FragmentHomeBinding
import kg.sunrise.infoapteka.enums.ImageRatioType
import kg.sunrise.infoapteka.enums.SortType
import kg.sunrise.infoapteka.networking.models.response.BannerResponse
import kg.sunrise.infoapteka.networking.models.response.ProductResponse
import kg.sunrise.infoapteka.ui.main.AuthBotomSheet
import kg.sunrise.infoapteka.ui.main.MainActivity
import kg.sunrise.infoapteka.ui.main.home.adapter.*
import kg.sunrise.infoapteka.ui.shared.imageAdapter.ImageAdapter
import kg.sunrise.infoapteka.ui.shared.imageAdapter.ImageAdapterDelegate
import kg.sunrise.infoapteka.ui.shared.preorderFragment.PreorderBottomSheet
import kg.sunrise.infoapteka.ui.shared.productDetailFragment.ProductDetailFragmentDirections
import kg.sunrise.infoapteka.ui.shared.productPaging.ProductAdapterDelegate
import kg.sunrise.infoapteka.ui.shared.productPaging.ProductPagingAdapter
import kg.sunrise.infoapteka.utils.constants.IMAGE_SCROLLING_DURATION
import kg.sunrise.infoapteka.utils.constants.preloadImagesCount
import kg.sunrise.infoapteka.utils.extensions.*
import kg.sunrise.infoapteka.utils.preference.isAuthorized
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment
    : BaseFragmentWithPaging<ProductResponse, FragmentHomeBinding, HomeViewModel>(),
    ImageAdapterDelegate, MainCardAdapterDelegate, ProductAdapterDelegate {

    override val viewModel: HomeViewModel by viewModel()
    override val progressBar: ConstraintLayout by lazy {
        binding.inclProgress.clProgress
    }

    override var isToShowProgress: Boolean = true
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it)
                switchOnGPS()
        }
    private val imageAdapter = ImageAdapter(this, ImageRatioType.MAIN_PAGE)
    private val imageCardAdapter = MainCardAdapter(this)
    private val productAdapter = ProductPagingAdapter(this)

    private val imageRunnable = Runnable {
        if (imageAdapter.itemCount == 0) return@Runnable

        if (binding.rvImages.currentItem == imageAdapter.itemCount - 1) {
            binding.rvImages.setCurrentItem(0, true)
        } else {
            binding.rvImages.setCurrentItem(binding.rvImages.currentItem + 1, true)
        }
    }

    private val handler = Handler()

    override fun makeRequests() {
        viewModel.clearDataIfNeed()

        getProducts()

        if (viewModel.cashedImages == null) {
            viewModel.getBanners()
        }
    }

    override fun findTypeOfObject(data: Any?) {
        isToShowProgress = true
        if ((data as? ArrayList<BannerResponse>) != null) {
            setupBanners(data)
        }
    }

    override fun successRequest() {
    }

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater)
    }

    override fun init() {
        setupUI()
        setupImageAdapter()
        setupCardAdapter()
        setupProductAdapter()
        initListeners()
    }

    private fun setupUI() {
        binding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            binding.swipeRefresh.isEnabled = verticalOffset == 0
        })
    }

    private fun initListeners() {

        binding.swipeRefresh.setOnRefreshListener {
            swipeRefresh()
        }

        binding.inclToolbar.ivNotification.setOnClickListener {
            if (isAuthorized(requireContext()))
                navigateToNotifications()
            else {
                AuthBotomSheet().show(parentFragmentManager, null)
            }
        }

        binding.inclToolbar.btnSearch.setOnClickListener {
            navigateToSearchHint()
        }
    }

    private fun swipeRefresh() {
        binding.swipeRefresh.isRefreshing = false
        viewModel.clearPaging()
        viewModel.cashedImages = null
        makeRequests()
    }

    private fun navigateToSearchHint() {
        val action = HomeFragmentDirections.actionGlobalSearchHingFragment()
        findNavController().navigate(action)
    }

    private fun navigateToNotifications() {
        val action = HomeFragmentDirections.actionGlobalToNotificationNavGraph()
        findNavController().navigate(action)
    }

    @SuppressLint("WrongConstant")
    private fun setupImageAdapter() {
        binding.rvImages.adapter = imageAdapter
        binding.rvImages.offscreenPageLimit = preloadImagesCount
        binding.rvImages.setOverScrollModeNever()
        binding.rvImages.setPageTransformer(
            MarginPageTransformer(
                resources.getDimensionPixelOffset(
                    R.dimen.dp_2_5x
                )
            )
        )

        binding.dotsIndicator.setViewPager2(binding.rvImages)

        binding.rvImages.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                handler.removeCallbacks(imageRunnable)
                handler.postDelayed(imageRunnable, IMAGE_SCROLLING_DURATION)
            }
        })

        viewModel.cashedImages?.let {
            setupBanners(it)
        }
    }

    private fun setupCardAdapter() {
        binding.rvInfo.adapter = imageCardAdapter

        imageCardAdapter.setData(
            arrayListOf(
                MainCardDTO(R.drawable.ic_pin, R.string.Map_pharmacy, R.string.Lorem),
                MainCardDTO(R.drawable.ic_main_card_2, R.string.Order, R.string.Lorem),
                MainCardDTO(R.drawable.ic_main_card_1, R.string.How_to_order, R.string.Lorem)
            )
        )
    }

    private fun setupProductAdapter() {
        binding.rvItems.adapter = productAdapter
        lifecycleScope.launch {
            productAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect()
        }
    }

    override fun onResume() {
        super.onResume()

        handler.removeCallbacks(imageRunnable)
        handler.postDelayed(imageRunnable, IMAGE_SCROLLING_DURATION)
    }

    override fun onPause() {
        super.onPause()

        handler.removeCallbacks(imageRunnable)
    }

    override fun onImageClick(position: Int) {
        handler.removeCallbacks(imageRunnable)

        try {
            viewModel.cashedImages?.let {
                val link = it[position]

                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(link.link)
                startActivity(i)
            }
        } catch (e: Exception) {
        }
    }

    override fun onCardClick(position: Int) {
        handler.removeCallbacks(imageRunnable)

        when (position) {
            CARD_PHARMACY_ADDRESS -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                navigate(HomeFragmentDirections.actionHomeFragmentToNavFarm())
            }
            CARD_DELIVERY -> findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDeliveryConditionFragment2())
            CARD_HOW_TO_ORDER -> findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDeliveryConditionFragment2())
        }
    }

    private fun switchOnGPS() {
        if (hasLocationPermission(requireContext())) {
            if (!checkIfGpsEnabled(requireContext())) {
                turnOnGPS(requireContext())
            }
        }
    }

    private fun getProducts() {
        requestJob = lifecycleScope.launch {
            viewModel.getProductPaging(
                filter = SortType.BESTSELLER.ordering,
                null,
                null,
                null,
                null
            ).collectLatest {
                productAdapter.submitData(it)
            }
        }
    }

    // delegate implementation
    override fun onProductClick(position: Int) {
        val item = productAdapter.getItemAtPosition(position)
        item?.let {
            handler.removeCallbacks(imageRunnable)
            val action =
                ProductDetailFragmentDirections.actionGlobalProductDetailFragment(productId = it.id)
            findNavController().navigate(action)
        }
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
            item?.let { it ->
                it.isInBasket = !it.isInBasket
                productAdapter.notifyItemChanged(position)

                isToShowProgress = false
                viewModel.addRemoveBasket(it.isInBasket, it.id)

                if (it.isInBasket) {
                    val activity = (requireActivity() as? MainActivity)
                    activity?.let { mainActivity ->
                        val badge =
                            mainActivity.binding.bottomNav.getOrCreateBadge(R.id.basket_nav_graph)
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
                    response.owner.phone
                )
                bottomSheet.show(childFragmentManager, null)
            }
        } else {
            AuthBotomSheet().show(parentFragmentManager, null)
        }
    }

    private fun setupBanners(images: ArrayList<BannerResponse>) {
        if (images.isNotEmpty()) {
            val imageURLS = images.mapNotNull { it.image }
            viewModel.cashedImages = images
            imageAdapter.setData(ArrayList(imageURLS))
            binding.rvImages.currentItem = 0
        }
    }

    companion object {

        private val CARD_PHARMACY_ADDRESS = 0
        private val CARD_DELIVERY = 1
        private val CARD_HOW_TO_ORDER = 2
    }
}

