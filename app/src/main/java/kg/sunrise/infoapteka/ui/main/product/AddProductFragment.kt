package kg.sunrise.infoapteka.ui.main.product

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toFile
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.esafirm.imagepicker.features.registerImagePicker
import com.theartofdev.edmodo.cropper.CropImage
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.fragment.BaseFragmentWithVM
import kg.sunrise.infoapteka.databinding.FragmentAddProductBinding
import kg.sunrise.infoapteka.ui.main.menu.profile.sellerProfile.SellerProfileFragment
import kg.sunrise.infoapteka.ui.main.menu.profile.sellerProfile.zoomAlert.ZoomAlertDialog
import kg.sunrise.infoapteka.utils.constants.*
import kg.sunrise.infoapteka.utils.extensions.*
import org.koin.androidx.viewmodel.ext.android.viewModel

open class AddProductFragment :
    BaseFragmentWithVM<FragmentAddProductBinding, ProductViewModel>(),
    PhotoDelegate {

    override val viewModel: ProductViewModel by viewModel()
    override val progressBar: ConstraintLayout by lazy {
        binding.inclProgress.clProgress
    }

    protected open val toolbarTitle by lazy {
        getString(R.string.adding_product)
    }
    protected open val okButtonTitle by lazy {
        getString(R.string.add_product)
    }
    private val photoSizePattern by lazy {
        getString(R.string.certificate_size_pattern)
    }
    protected val productAdapter by lazy {
        PhotoAdapter(this)
    }
    private var typeImage =
        SellerProfileFragment.ImageTypeForCrop.EmptyImage

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentAddProductBinding.inflate(inflater)

    override fun init() {
        initViews()
        initListeners()
        initObservers()
    }

    private fun initViews() = with(binding) {
        initOldValues()
        etCategory.setLayoutEnabled(false)
        etProductPrice.setInputLength(MAX_PRICE_LENGTH)
        loadFileImageView.setTitle(getString(R.string.product_photo))
        inclToolbar.tvToolbar.text = toolbarTitle
        btnOk.text = okButtonTitle
        initRV()
    }

    protected fun initOldValues() = with(binding) {
        etCategory.setIfNotNull(viewModel.categoryLD.value?.title)
        etProductName.setIfNotNull(viewModel.productNameLD.value)
        viewModel.productDescriptionLD.value?.let {
            etProductDescription.setText(it)
        }
        viewModel.productPriceLD.value?.let {
            etProductPrice.setInputText(
                if (it < 0) ""
                else it.toString()
            )
        }
    }

    private fun initRV() = with(binding.rvPhoto) {
        adapter = productAdapter
    }

    private fun initListeners() = with(binding) {
        inclToolbar.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        initCategoryListener()

        etProductName.addTextChangedListener {
            viewModel.productNameLD.value = it.text.toString()
        }

        etProductDescription.doAfterTextChanged {
            viewModel.productDescriptionLD.value =
                it?.toString() ?: ""
        }

        initPriceListener()

        initClickForCertificateItem()

        btnOk.setOnClickListener {
            onOkButtonClicked()
        }
    }

    private fun initCategoryListener() {
        setFragmentResultListener(
            REQUEST_KEY_CATEGORY,
            this::categoryResultListener
        )

        binding.etCategory.setOnLayoutClickListener {
            navigateToCategoryChoosing()
        }
    }

    protected open fun navigateToCategoryChoosing() {
        val action = AddProductFragmentDirections
            .actionAddProductFragmentToCategoryChooseFragment(
                categoryType = AddProductCategoryType
            )
        findNavController().navigate(action)
    }

    private fun categoryResultListener(
        requestKey: String,
        bundle: Bundle
    ) {
        bundle.getParcelable<Category>(KEY_CATEGORY)?.let {
            binding.etCategory.setInputText(it.title)
            viewModel.categoryLD.value = it
        }
    }

    private fun initPriceListener() = with(binding.etProductPrice) {
        addTextChangedListener {
            val price =
                if (it.text.toString().isBlank()) "0"
                else it.text.toString()
            viewModel.productPriceLD.value = price.toInt()
        }
    }

    protected open fun onOkButtonClicked() {
        viewModel.postProduct()
    }

    private fun initClickForCertificateItem() {
        val launcher = registerImagePicker {
            typeImage = SellerProfileFragment
                .ImageTypeForCrop.SertificateImage
            it.forEach { i ->
                CropImage.activity(i.uri).start(
                    requireContext(), this
                )
            }
        }
        binding.loadFileImageView.setClickListenerOnUpload {
            launcher.launch()
        }
    }

    private fun initObservers() = with(viewModel) {
        imagesLD.observe(viewLifecycleOwner) {
            productAdapter.submitList(ArrayList(it))
        }

        isAllRequirementsFilledLiveData.observe(viewLifecycleOwner) {
            if (it) {
                binding.btnOk.setEnabled(R.color.green)
            } else {
                binding.btnOk.setDisabled(R.color.secondary_gray)
            }
        }
    }

    override fun makeRequests() {
    }

    override fun findTypeOfObject(data: Any?) {
        when (data) {
            IMAGES_UPLOADED_SUCCESSFULLY -> {
                imageUploadedSuccessfully()
            }
            IMAGES_UPLOADED_FAILED -> imageUploadingFailed()
        }
    }

    protected open fun imageUploadedSuccessfully() {
        snackbar(
            binding.root,
            R.string.product_added_successfully
        )
        findNavController().navigate(
            R.id.action_addProductFragment_to_myProductsFragment
        )
    }

    protected open fun imageUploadingFailed() {
        snackbar(
            binding.root,
            R.string.image_uploaded_failed_try_again
        )
        val action = AddProductFragmentDirections
            .actionAddProductFragmentToEditProductFragment(
                viewModel.drugId
            )
        findNavController().navigate(action)
    }

    override fun successRequest() {
        viewModel.uploadImages()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                when (typeImage) {
                    SellerProfileFragment.ImageTypeForCrop.SertificateImage -> {
                        onCertificateImageAdded(result.uri)
                    }
                    else -> {
                        throw IllegalStateException(result.error)
                    }

                }
            }
        }
    }

    private fun onCertificateImageAdded(uri: Uri) {
        val file = uri.toFile()
        val image = PhotoUri(
            uri,
            file.name,
            String.format(
                photoSizePattern,
                file.length().toMb()
            )
        )
        viewModel.addImage(image)
    }

    override fun onItemClick(item: Photo) {
        zoomCertificate(item)
    }

    private fun zoomCertificate(image: Photo) {
        val imageAddress = when (image) {
            is PhotoUri -> image.imageUri.toString()
            is PhotoUrl -> image.imageUrl
        }
        ZoomAlertDialog(imageAddress).show(
            parentFragmentManager,
            null
        )
    }

    override fun onDeleteClick(item: Photo) {
        viewModel.removeImage(item)
    }

}