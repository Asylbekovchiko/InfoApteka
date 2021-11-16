package kg.sunrise.infoapteka.ui.main.menu.myProducts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.fragment.BaseFragmentWithPaging
import kg.sunrise.infoapteka.databinding.FragmentMyProductsBinding
import kg.sunrise.infoapteka.ui.main.menu.myProducts.adapter.MyProductAdapter
import kg.sunrise.infoapteka.ui.main.menu.myProducts.adapter.MyProductDTO
import kg.sunrise.infoapteka.utils.constants.DRUG_DELETED_SUCCESSFULLY
import kg.sunrise.infoapteka.utils.extensions.gone
import kg.sunrise.infoapteka.utils.extensions.snackbar
import kg.sunrise.infoapteka.utils.extensions.visible
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyProductsFragment :
    BaseFragmentWithPaging<MyProductDTO,
            FragmentMyProductsBinding,
            MyProductsViewModel>(),
    MyProductDetailBottomSheetFragmentDelegate {

    override val viewModel: MyProductsViewModel by viewModel()
    override val progressBar: ConstraintLayout by lazy {
        binding.inclProgress.clProgress
    }

    private var detailsBottomSheet: MyProductDetailBottomSheetFragment? =
        null

    private val productAdapter = MyProductAdapter() { position ->
        onDetailClicked(position)
    }

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMyProductsBinding {
        return FragmentMyProductsBinding.inflate(inflater)
    }

    override fun init() {
        initBottomSheet()
        initListeners()
        setupUI()
        setupAdapter()
    }

    private fun setupUI() {
        binding.inclToolbar.tvToolbar.setText(R.string.My_products)
        binding.inclToolbar.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun initBottomSheet() {
        detailsBottomSheet = MyProductDetailBottomSheetFragment()
        detailsBottomSheet?.configure(this)
    }

    private fun initListeners() = with(binding) {
        btnAddProduct.setOnClickListener {
            findNavController().navigate(
                R.id.action_myProductsFragment_to_addProductFragment
            )
        }
    }

    override fun makeRequests() {
        viewModel.clearDataIfNeed()
        getMyProducts()
    }

    private fun getMyProducts() {
        requestJob = lifecycleScope.launch {
            viewModel.getMyProductPaging().collectLatest {
                productAdapter.submitData(it)
            }
        }

        productAdapter.addLoadStateListener {
            val isItemsEmpty = it.refresh is LoadState.NotLoading && productAdapter.itemCount == 0

            if (isItemsEmpty) {
                binding.clEmpty.visible()
            } else {
                binding.clEmpty.gone()
            }
        }
    }

    override fun findTypeOfObject(data: Any?) {
        if (data == DRUG_DELETED_SUCCESSFULLY) onItemDeleted()
    }

    private fun onItemDeleted() {
        detailsBottomSheet?.dismiss()
        snackbar(binding.root, R.string.product_deleted_successfully)
        viewModel.clearPaging()
        getMyProducts()
    }

    override fun successRequest() {
    }

    private fun setupAdapter() {
        binding.rvItems.adapter = productAdapter


    }

    override fun onDestroyView() {
        detailsBottomSheet = null
        super.onDestroyView()
    }

    override fun onRemoveMyProductClick(position: Int) {
        viewModel.deleteDrug(
            productAdapter.getItemAt(position)?.id ?: -1
        )
    }

    override fun onEditMyProductClick(position: Int) {
        val action = MyProductsFragmentDirections
            .actionMyProductsFragmentToEditProductFragment(
                productAdapter.getItemAt(position)?.id ?: -1
            )
        findNavController().navigate(action)
    }

    private fun onDetailClicked(position: Int) {
        detailsBottomSheet?.show(position, childFragmentManager, null)
    }
}