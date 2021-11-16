package kg.sunrise.infoapteka.ui.main.product.editProduct

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.networking.models.response.EditDrugResponse
import kg.sunrise.infoapteka.ui.main.product.AddProductFragment
import kg.sunrise.infoapteka.ui.main.product.EditProductCategoryType
import kg.sunrise.infoapteka.utils.constants.DRUG_DELETED_SUCCESSFULLY
import kg.sunrise.infoapteka.utils.extensions.snackbar
import kg.sunrise.infoapteka.utils.extensions.visible
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditProductFragment : AddProductFragment() {

    private val args by navArgs<EditProductFragmentArgs>()

    override val viewModel: EditProductViewModel by viewModel()

    override val toolbarTitle by lazy {
        getString(R.string.editing_product)
    }
    override val okButtonTitle by lazy {
        getString(R.string.save)
    }

    override fun init() {
        super.init()
        initViews()
        initListeners()
    }

    private fun initViews() = with(binding) {
        btnDelete.visible()
    }

    private fun initListeners() = with(binding) {
        btnDelete.setOnClickListener {
            viewModel.deleteDrug()
        }
    }

    override fun onOkButtonClicked() {
        viewModel.putDrug()
    }

    override fun navigateToCategoryChoosing() {
        val action = EditProductFragmentDirections
            .actionEditProductFragmentToCategoryChooseFragment(
                categoryType = EditProductCategoryType
            )
        findNavController().navigate(action)
    }

    override fun makeRequests() {
        viewModel.getDrug(args.drugId)
    }

    override fun findTypeOfObject(data: Any?) {
        super.findTypeOfObject(data)
        when (data) {
            is EditDrugResponse -> onEditDrugResponse(data)
            DRUG_DELETED_SUCCESSFULLY -> drugDeletedSuccessfully()
        }
    }

    private fun onEditDrugResponse(
        drug: EditDrugResponse
    ) = with(binding) {
        viewModel.categoryLD.value = drug.category
        etCategory.setInputText(drug.category.title)
        etProductName.setInputText(drug.name)
        etProductDescription.setText(drug.instructionTxt)
        etProductPrice.setInputText(drug.price.toString())
    }

    private fun drugDeletedSuccessfully() {
        findNavController().navigate(
            R.id.action_editProductFragment_to_myProductsFragment
        )
        snackbar(binding.root, R.string.product_deleted_successfully)
    }

    override fun imageUploadedSuccessfully() {
        findNavController().navigate(
            R.id.action_editProductFragment_to_myProductsFragment
        )
        snackbar(binding.root, R.string.drug_updated_successfully)
    }

    override fun imageUploadingFailed() {
        snackbar(
            binding.root,
            R.string.image_uploaded_failed_try_again
        )
    }
}