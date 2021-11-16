package kg.sunrise.infoapteka.ui.main.basket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.fragment.BaseFragmentWithVM
import kg.sunrise.infoapteka.databinding.FragmentBasketBinding
import kg.sunrise.infoapteka.networking.models.response.BasketResponse
import kg.sunrise.infoapteka.networking.models.response.CartItem
import kg.sunrise.infoapteka.ui.main.AuthBotomSheet
import kg.sunrise.infoapteka.ui.main.MainActivity
import kg.sunrise.infoapteka.ui.main.basket.adapter.BasketDelegate
import kg.sunrise.infoapteka.ui.main.basket.adapter.BasketListAdapter
import kg.sunrise.infoapteka.utils.enums.OrderingResult
import kg.sunrise.infoapteka.utils.extensions.*
import kg.sunrise.infoapteka.utils.preference.isAuthorized
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class BasketFragment :
    BaseFragmentWithVM<FragmentBasketBinding, BasketViewModel>(), BasketDelegate,
    OrderingResultListener {
    private val basketAdapter = BasketListAdapter(this)
    override var isToShowProgress: Boolean = false
    override val viewModel: BasketViewModel by viewModel()
    override val progressBar: ConstraintLayout by lazy {
        binding.inclProgress.clProgress
    }

    private val args by navArgs<BasketFragmentArgs>()
    private val orderingResultBottomSheet by lazy {
        OrderingResultBottomSheetFragment(
            args.isOrderingSuccess,
            this
        )
    }

    companion object {
        const val noResultItem = 0
        const val singleItemResult = 1
        val numberItemResult = 2..4
    }

    override fun findTypeOfObject(data: Any?) {
        when (data) {
            is BasketResponse -> {
                setupUI(data)
                initListeners(data)
            }
        }
    }

    private fun setupUI(data: BasketResponse) {
        binding.swipeRefreshLayout.isRefreshing = false
        if (data.countOfItems > 0) {
            binding.cvBasketTotal.setVisibility(true)
            binding.cvBottomOrder.visible()
        } else {
            binding.cvBasketTotal.setVisibility(false)
            binding.cvBottomOrder.gone()
        }
        initAdapter(data.cartItems)
        initToolbar(data)
        binding.cvBasketTotal.setContent(data.getTotalValue, data.countOfItems)
    }

    private fun initAdapter(data: ArrayList<CartItem>) {
        binding.rvItemBasket.adapter = basketAdapter
        basketAdapter.submitList(data)
    }

    override fun successRequest() {
        viewModel.getBasketItems()
    }

    override fun makeRequests() {
        viewModel.getBasketItems()
    }

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentBasketBinding {
        return FragmentBasketBinding.inflate(inflater)
    }

    override fun init() {
        initAfterOrdering()
        binding.inclToolbar.tvToolbar.text = getString(R.string.Basket)
        binding.inclToolbar.ivBack.gone()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val activity = (requireActivity() as? MainActivity)
        activity?.let {
            val badge = activity.binding.bottomNav.getOrCreateBadge(R.id.basket_nav_graph)
            badge.isVisible = false
        }
        return super.onCreateView(inflater, container, savedInstanceState)

    }

    private fun initListeners(data: BasketResponse) {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getBasketItems()
        }
        binding.cvBasketTotal.setOnDeleiveryClick {
            val action = BasketFragmentDirections.actionBasketFragmentToDeliveryConditionFragment()
            navigate(action)
        }
        binding.btnMakeOrder.setOnClickListener {
            if (!isAuthorized(requireContext()))
                AuthBotomSheet().show(parentFragmentManager, null)
            else {
                val action = BasketFragmentDirections.actionBasketFragmentToOrderFragment(
                    data.id,
                    data.countOfItems,
                    data.getTotalValue.toInt(),
                null
                )
                navigate(action)
            }
        }
    }

    private fun initToolbar(data: BasketResponse) {
        binding.inclToolbar.ivBack.gone()
        when (data.countOfItems) {
            noResultItem -> {
                binding.inclToolbar.tvToolbar.text = getString(R.string.Basket)
                binding.inclEmptyItem.clContent.visible()
            }
            singleItemResult -> setDefaultTextStyleDivider(
                getString(
                    R.string.Basket_Count_One,
                    data.countOfItems
                )
            )
            in numberItemResult ->
                setDefaultTextStyleDivider(
                    getString(
                        R.string.Basket_Count_Less_Four,
                        data.countOfItems
                    )
                )
            else -> setDefaultTextStyleDivider(
                getString(
                    R.string.Basket_Count_Item,
                    data.countOfItems
                )
            )
        }
    }

    private fun setDefaultTextStyleDivider(type: String) {
        binding.inclToolbar.tvToolbar.textStyleDivider(type, 7, R.color.secondary_gray, 18)
    }

    override fun onIncreaseItemPosition(position: Int) {
        viewModel.addItemToBasket(basketAdapter.currentList[position].drug.id)
    }

    override fun onDecreaseItemPosition(position: Int) {
        viewModel.removeItemFromBasket(basketAdapter.currentList[position].drug.id)
    }

    override fun onDeleteItemClickListener(position: Int) {
        viewModel.removeFromBasket(basketAdapter.currentList[position].drug.id)
        var items = ArrayList(basketAdapter.currentList)
        items.removeAt(position)
        basketAdapter.submitList(items)
    }

    override fun onFavouriteClickListener(position: Int, isFavourite: Boolean) {
        if (isFavourite)
            viewModel.removeFromFavourites(basketAdapter.currentList[position].drug.id)
        else
            viewModel.addToFavourites(basketAdapter.currentList[position].drug.id)
    }

    private fun initAfterOrdering() {
        setUpOrderingResultBottomSheet()
    }

    private fun setUpOrderingResultBottomSheet() {
        if (args.isOrderingSuccess != OrderingResult.NON_ORDERING) {
            orderingResultBottomSheet.show(childFragmentManager, null)
        }

    }

    override fun orderingResultBtnClicked() {
        when (args.isOrderingSuccess) {
            OrderingResult.SUCCESS -> navigateToMain()
            OrderingResult.FAILURE -> findNavController().navigateUp()

        }

    }


}