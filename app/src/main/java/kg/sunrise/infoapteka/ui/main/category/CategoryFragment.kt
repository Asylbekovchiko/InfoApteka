package kg.sunrise.infoapteka.ui.main.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.fragment.BaseFragmentWithVM
import kg.sunrise.infoapteka.databinding.FragmentCategoryBinding
import kg.sunrise.infoapteka.enums.CategoryType
import kg.sunrise.infoapteka.networking.models.response.CategoryItemResponse
import kg.sunrise.infoapteka.ui.main.AuthBotomSheet
import kg.sunrise.infoapteka.ui.main.category.adapter.CategoryAdapter
import kg.sunrise.infoapteka.ui.main.category.adapter.CategoryAdapterDelegate
import kg.sunrise.infoapteka.ui.main.home.HomeFragmentDirections
import kg.sunrise.infoapteka.utils.constants.defaultIntValue
import kg.sunrise.infoapteka.utils.extensions.gone
import kg.sunrise.infoapteka.utils.extensions.visible
import kg.sunrise.infoapteka.utils.preference.isAuthorized
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

open class CategoryFragment :
    BaseFragmentWithVM<FragmentCategoryBinding, CategoryViewModel>(),
    CategoryAdapterDelegate {

    override val viewModel: CategoryViewModel by sharedViewModel()
    override val progressBar: ConstraintLayout by lazy {
        binding.inclProgress.clProgress
    }

    private lateinit var categoryType: CategoryType

    private val navArgs by navArgs<CategoryFragmentArgs>()

    private val categoryAdapter = CategoryAdapter(this)

    override fun makeRequests() {
        if (categoryType == CategoryType.MAIN_CATEGORY) {
            viewModel.getCategories()
        }
    }

    override fun findTypeOfObject(data: Any?) {
        if ((data as? ArrayList<CategoryItemResponse>) != null) {
            viewModel.currentCategories = data
            categoryAdapter.setData(viewModel.currentCategories)
            viewModel.currentCategories = viewModel.currentCategories
        }
    }

    override fun successRequest() {
    }

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCategoryBinding {
        return FragmentCategoryBinding.inflate(inflater)
    }

    override fun init() {
        setupUI()
        setupAdapter()
        initListeners()
    }

    private fun initListeners() {
        binding.apply {
            tvMainTitle.setOnClickListener { findNavController().navigateUp() }

            tvViewAll.setOnClickListener {
                navigateToSearchResults(navArgs.subCategoryID, navArgs.categoryFullName)
            }

            inclToolbar.apply {
                ivNotification.setOnClickListener {
                    if (isAuthorized(requireContext()))
                        navigateToNotifications()
                    else {
                        AuthBotomSheet().show(parentFragmentManager, null)
                    }
                }

                btnSearch.setOnClickListener {
                    navigateToSearchHint()
                }
            }
        }
    }

    protected open fun setupUI() {
        categoryType =
            if (navArgs.subCategoryID == defaultIntValue) CategoryType.MAIN_CATEGORY else CategoryType.SUB_CATEGORY

        when (categoryType) {
            CategoryType.MAIN_CATEGORY -> binding.groupHeader.gone()
            CategoryType.SUB_CATEGORY -> {
                binding.apply {
                    groupHeader.visible()
                    tvMainTitle.text = navArgs.mainCategoryTitle
                    tvCurrentTitle.text = navArgs.subCategoryTitle
                }
            }
        }
    }

    private fun setupAdapter() {
        binding.rvItems.adapter = categoryAdapter

        //todo remove from here and add after reqeust
        when (categoryType) {
            CategoryType.MAIN_CATEGORY -> {

            }
            CategoryType.SUB_CATEGORY -> {
                categoryAdapter.setData(viewModel.currentCategories)
            }
        }
    }

    override fun onCategoryClick(categoryID: Int) {
        val currentSubCategory = viewModel.currentCategories.first { it.id == categoryID }
        val currentCategoryName = if (navArgs.categoryFullName.isEmpty()) currentSubCategory.name else navArgs.categoryFullName + ", ${currentSubCategory.name}"
        if (currentSubCategory.items.isNullOrEmpty()) {
            navigateToSearchResults(currentSubCategory.id, currentCategoryName)
            return
        }

        viewModel.currentCategories = currentSubCategory.items
        when (categoryType) {
            CategoryType.MAIN_CATEGORY -> {
                navigateToNextCategory(currentSubCategory.id, currentSubCategory.name, getString(R.string.Categories), currentSubCategory.name)
            }
            CategoryType.SUB_CATEGORY -> {
                navigateToNextCategory(currentSubCategory.id, currentSubCategory.name, navArgs.subCategoryTitle, currentCategoryName)
            }
        }
    }

    protected open fun navigateToSearchResults(currentSubCategory: Int, categoryName: String) {
        val action =
            CategoryFragmentDirections.actionCategoryFragmentToSearchFragment(
                categoryID = currentSubCategory,
                categoryName = categoryName
            )
        findNavController().navigate(action)
    }

    private fun navigateToNotifications() {
        val action = HomeFragmentDirections.actionGlobalToNotificationNavGraph()
        findNavController().navigate(action)
    }

    protected open fun navigateToNextCategory(
        subCategoryID: Int,
        subCategoryTitle: String,
        mainCategoryTitle: String,
        categoryFullName: String
    ) {
        val action =
            CategoryFragmentDirections.actionGlobalCategoryFragment(
                subCategoryID,
                subCategoryTitle,
                mainCategoryTitle,
                categoryFullName
            )

        findNavController().navigate(action)
    }

    private fun navigateToSearchHint() {
        val action = CategoryFragmentDirections.actionGlobalSearchHingFragment()
        findNavController().navigate(action)
    }
}