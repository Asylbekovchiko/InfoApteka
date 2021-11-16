package kg.sunrise.infoapteka.ui.shared.productDetailFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.fragment.BaseFragmentWithVM
import kg.sunrise.infoapteka.databinding.FragmentProductDetailBinding
import kg.sunrise.infoapteka.enums.ImageRatioType
import kg.sunrise.infoapteka.networking.models.response.ProductDetailResponse
import kg.sunrise.infoapteka.networking.models.response.ProductResponse
import kg.sunrise.infoapteka.ui.main.AuthBotomSheet
import kg.sunrise.infoapteka.ui.main.MainActivity
import kg.sunrise.infoapteka.ui.main.home.HomeViewModel
import kg.sunrise.infoapteka.ui.shared.imageAdapter.ImageAdapter
import kg.sunrise.infoapteka.ui.shared.imageAdapter.ImageAdapterDelegate
import kg.sunrise.infoapteka.ui.shared.preorderFragment.PreorderBottomSheet
import kg.sunrise.infoapteka.ui.shared.productDetailFragment.adapter.Instruction
import kg.sunrise.infoapteka.ui.shared.productDetailFragment.adapter.InstructionAdapter
import kg.sunrise.infoapteka.ui.shared.productDetailFragment.adapter.ProductAdapter
import kg.sunrise.infoapteka.ui.shared.productPaging.ProductAdapterDelegate
import kg.sunrise.infoapteka.utils.extensions.gone
import kg.sunrise.infoapteka.utils.extensions.setDisabled
import kg.sunrise.infoapteka.utils.extensions.setOverScrollModeNever
import kg.sunrise.infoapteka.utils.extensions.visible
import kg.sunrise.infoapteka.utils.preference.isAuthorized
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductDetailFragment
    : BaseFragmentWithVM<FragmentProductDetailBinding, HomeViewModel>(),
    ProductAdapterDelegate, ImageAdapterDelegate {

    override val viewModel: HomeViewModel by viewModel()
    override val progressBar: ConstraintLayout by lazy {
        binding.inclProgress.clProgress
    }

    override var isToShowProgress: Boolean = true

    private val navArgs by navArgs<ProductDetailFragmentArgs>()

    private val bestsellersAdapter = ProductAdapter(this)
    private val imagesAdapter = ImageAdapter(this, ImageRatioType.PRODUCT_DETAIL)
    private val instructionsAdapter = InstructionAdapter()

    override fun init() {
        initListeners()
        setupImageAdapter()
        setupInstructionsAdapter()
        setupBestsellersAdapter()
    }

    private fun initListeners() {
        binding.apply {
            inclToolbar.apply {
                ivBack.setOnClickListener { findNavController().navigateUp() }
            }

            tvOwner.setOnClickListener {
                val ownerInfo = viewModel.productDetail
                if (ownerInfo?.isOwner == false) {
                    ownerInfo.let {
                        val action =
                            ProductDetailFragmentDirections.actionGlobalSellerProducts(it.owner.id)
                        findNavController().navigate(action)
                    }
                }

            }

            cardFavourite.setOnClickListener {
                val product = viewModel.productDetail
                if (isAuthorized(requireContext())) {
                    product?.let {
                        it.isFavorite = !it.isFavorite
                        ivFavorite.setImageResource(if (it.isFavorite) R.drawable.ic_card_heart_checked else R.drawable.ic_card_heart_unchecked)

                        isToShowProgress = false
                        viewModel.addRemoveFavourite(it.isFavorite, it.id)
                    }
                } else {
                    AuthBotomSheet().show(parentFragmentManager, null)
                }
            }

            btnPreorder.setOnClickListener {
                val ownerInfo = viewModel.productDetail
                if (isAuthorized(requireContext())) {
                    if (ownerInfo != null && ownerInfo.owner.address != null && ownerInfo.owner.phone != null) {
                        val bottomSheet = PreorderBottomSheet(
                            ownerInfo.owner.firstName,
                            ownerInfo.owner.address,
                            ownerInfo.owner.phone
                        )
                        bottomSheet.show(childFragmentManager, null)
                    }
                } else {
                    AuthBotomSheet().show(parentFragmentManager, null)
                }
            }

            btnBasket.setOnClickListener {
                addRemoveToBasket()
            }

            btnInBasket.setOnClickListener {
                addRemoveToBasket()
            }
        }
    }

    private fun addRemoveToBasket() {
        val product = viewModel.productDetail
        if (isAuthorized(requireContext())) {
            product?.let {
                it.isInBasket = !it.isInBasket
                val activity = (requireActivity() as? MainActivity)

                isToShowProgress = false
                viewModel.addRemoveBasket(it.isInBasket, it.id)

                binding.apply {
                    if (it.isInBasket) {
                        btnBasket.gone()
                        btnInBasket.visible()

                        activity?.let { mainActivity ->
                            val badge = mainActivity.binding.bottomNav.getOrCreateBadge(R.id.basket_nav_graph)
                            badge.isVisible = true

                            mainActivity.showAddItemSnackbar()
                        }
                    } else {
                        btnBasket.visible()
                        btnInBasket.gone()
                    }
                }
            }
        } else {
            AuthBotomSheet().show(parentFragmentManager, null)
        }
    }

    private fun setupImageAdapter() {
        binding.rvImages.adapter = imagesAdapter
        binding.rvImages.setOverScrollModeNever()
        binding.dotsIndicator.setViewPager2(binding.rvImages)
    }

    private fun setupInstructionsAdapter() {
        binding.rvInstructions.adapter = instructionsAdapter
        val divider = ContextCompat.getDrawable(requireContext(), R.drawable.shape_light_divider)
        val dividerItemDecoration =
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        divider?.let {
            dividerItemDecoration.setDrawable(it)
            binding.rvInstructions.addItemDecoration(dividerItemDecoration)
        }
    }

    private fun setupBestsellersAdapter() {
        binding.rvBestsellerProducts.adapter = bestsellersAdapter
    }

    override fun makeRequests() {
        if (viewModel.productDetail != null) {
            viewModel.productDetail?.let {
                fillProductDetailInfo(it)
            }
        } else {
            viewModel.getProductDetail(navArgs.productId)
        }
    }

    override fun findTypeOfObject(data: Any?) {
        isToShowProgress = true
        when (data) {
            is ProductDetailResponse -> {
                fillProductDetailInfo(data)
            }
        }
    }

    private fun fillProductDetailInfo(productDetail: ProductDetailResponse) {
        viewModel.productDetail = productDetail
        val images = productDetail.images.mapNotNull { it.image }
        fillImages(ArrayList(images))
        fillContent(productDetail)
        fillInstructions(productDetail.instructions, productDetail.instructionText)
        fillBestsellers(productDetail.products)
    }

    private fun fillImages(images: ArrayList<String>) {
        imagesAdapter.setData(images)
    }

    private fun fillContent(product: ProductDetailResponse) {
        binding.apply {
            tvOwner.text = product.owner.firstName
            tvProductName.text = product.productName
            tvProductPrice.text = getString(R.string.S_som, product.price)

            btnBasket.text = getString(R.string.Buy_for_s_som, product.price)

            ivFavorite.setImageResource(if (product.isFavorite) R.drawable.ic_card_heart_checked else R.drawable.ic_card_heart_unchecked)

            if (product.isOwner) {
                btnInBasket.gone()
                btnPreorder.gone()
                btnBasket.setDisabled(R.color.secondary_gray_2)
            } else {
                if (product.isInBasket) {
                    btnBasket.gone()
                    btnPreorder.gone()
                    btnInBasket.visible()
                } else {
                    if (product.isAvailable) {
                        btnBasket.visible()
                        btnPreorder.gone()
                    } else {
                        btnBasket.gone()
                        btnPreorder.visible()
                    }
                }
            }
        }
    }

    private fun fillInstructions(instructions: ArrayList<Instruction>, instruction: String?) {
        if (!instruction.isNullOrBlank()) {
            binding.tvInstruction.apply {
                visible()
                text = instruction
            }
        } else {
            binding.rvInstructions.visible()
            instructionsAdapter.setData(instructions)
        }
    }

    private fun fillBestsellers(products: ArrayList<ProductResponse>) {
        bestsellersAdapter.setData(products)
    }

    override fun successRequest() {
    }

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProductDetailBinding {
        return FragmentProductDetailBinding.inflate(inflater)
    }

    // implementation product delegate
    override fun onProductClick(position: Int) {
        val item = bestsellersAdapter.getItem(position)
        item.let {
            val action =
                ProductDetailFragmentDirections.actionGlobalProductDetailFragment(productId = it.id)
            findNavController().navigate(action)
        }
    }

    override fun onFavoriteClick(position: Int) {
        val item = bestsellersAdapter.getItem(position)
        if (isAuthorized(requireContext())) {
            item.let {
                it.isFavourite = !it.isFavourite
                bestsellersAdapter.notifyItemChanged(position)

                isToShowProgress = false
                viewModel.addRemoveFavourite(it.isFavourite, it.id)
            }
        } else {
            AuthBotomSheet().show(parentFragmentManager, null)
        }
    }

    override fun onBasketClick(position: Int) {
        val item = bestsellersAdapter.getItem(position)
        if (isAuthorized(requireContext())) {
            item.let {
                it.isInBasket = !it.isInBasket
                bestsellersAdapter.notifyItemChanged(position)

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
    }

    // implementation image delegate
    override fun onImageClick(position: Int) {
        val imageURL = imagesAdapter.getItem(position)
        val action =
            ProductDetailFragmentDirections.actionProductDetailFragmentToZoomFragment(imageURL)
        findNavController().navigate(action)
    }
}