package kg.sunrise.infoapteka.ui.main.product

import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.networking.models.response.mapToCategory
import kg.sunrise.infoapteka.ui.main.category.CategoryFragment
import kg.sunrise.infoapteka.utils.constants.KEY_CATEGORY
import kg.sunrise.infoapteka.utils.constants.REQUEST_KEY_CATEGORY
import kg.sunrise.infoapteka.utils.extensions.gone

class CategoryChooseFragment : CategoryFragment() {

    private val args by navArgs<CategoryChooseFragmentArgs>()

    override fun setupUI() = with(binding) {
        super.setupUI()
        clCategories.setPadding(0, 0, 0, 0)
        inclToolbar.cvToolbar.gone()
        tvViewAll.gone()
    }

    override fun navigateToSearchResults(currentSubCategory: Int, categoryName: String) {
        setResult(currentSubCategory)
        findNavController().popBackStack(
            getDestination(),
            false
        )
    }

    private fun getDestination() = when (args.categoryType) {
        AddProductCategoryType -> R.id.addProductFragment
        EditProductCategoryType -> R.id.editProductFragment
    }

    private fun setResult(currentSubCategory: Int) {
        val category = viewModel.currentCategories.find {
            it.id == currentSubCategory
        }?.mapToCategory()
        val bundle = bundleOf(
            KEY_CATEGORY to category
        )
        setFragmentResult(REQUEST_KEY_CATEGORY, bundle)
    }

    override fun navigateToNextCategory(
        subCategoryID: Int,
        subCategoryTitle: String,
        mainCategoryTitle: String,
        categoryFullName: String
    ) {

        val action = CategoryChooseFragmentDirections
            .actionCategoryChooseFragmentSelf(
                subCategoryID,
                subCategoryTitle,
                mainCategoryTitle,
                args.categoryType
            )
        findNavController().navigate(action)
    }
}