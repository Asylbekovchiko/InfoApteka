package kg.sunrise.infoapteka.ui.main.menu.ordersHistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.fragment.BaseFragmentWithVM
import kg.sunrise.infoapteka.databinding.FragmentOrdersHistoryBinding
import kg.sunrise.infoapteka.ui.main.home.HomeViewModel
import kg.sunrise.infoapteka.ui.main.menu.ordersHistory.adapter.OrderHistoryAdapter
import kg.sunrise.infoapteka.ui.main.menu.ordersHistory.adapter.OrderHistoryAdapterDelegate
import kg.sunrise.infoapteka.ui.main.menu.ordersHistory.adapter.OrderHistoryDTO
import kg.sunrise.infoapteka.utils.extensions.gone
import kg.sunrise.infoapteka.utils.extensions.visible
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrdersHistoryFragment : BaseFragmentWithVM<FragmentOrdersHistoryBinding, HomeViewModel>(),
    OrderHistoryAdapterDelegate {

    override val viewModel: HomeViewModel by viewModel()
    override val progressBar: ConstraintLayout by lazy {
        binding.inclProgress.clProgress
    }

    private val historyAdapter = OrderHistoryAdapter(this)

    override fun init() {
        setupUI()
        initListeners()
        setupAdapter()
    }

    private fun setupUI() {
        binding.inclToolbar.tvToolbar.setText(R.string.Orders_history)
    }

    private fun initListeners() {
        binding.inclToolbar.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupAdapter() {
        binding.rvItems.adapter = historyAdapter
    }

    override fun makeRequests() {
        viewModel.getMyOrderHistory()
    }

    override fun findTypeOfObject(data: Any?) {
        if ((data as? ArrayList<OrderHistoryDTO>) != null) {
            fillOrderHistory(data)
        }
    }

    private fun fillOrderHistory(orders: ArrayList<OrderHistoryDTO>) {
        if (orders.isEmpty()) {
            binding.clEmpty.visible()
            binding.rvItems.gone()
        } else {
            binding.clEmpty.gone()
            binding.rvItems.visible()

            val orders = orders.mapIndexed { index, orderHistoryDTO ->
                orderHistoryDTO.isExpanded = index == 0
                return@mapIndexed orderHistoryDTO
            }
            historyAdapter.setData(ArrayList(orders))
        }
    }

    override fun successRequest() {
    }

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentOrdersHistoryBinding {
        return FragmentOrdersHistoryBinding.inflate(inflater)
    }

    // delegate
    override fun scrollToPosition(position: Int) {
        binding.rvItems.smoothScrollToPosition(position)
    }

    override fun setExpanded(
        isExpanded: Boolean,
        position: Int
    ) {
        val item = historyAdapter.getItemByPosition(position)
        item.isExpanded = isExpanded
    }

    override fun onReorderClick(
        orderId: Int,
        itemsCount: Int,
        totalSum: Int,
        orderID: String
    ) {
        val action = OrdersHistoryFragmentDirections.actionOrdersHistoryFragmentToOrderingFragment(
            orderId,
            itemsCount,
            totalSum,
            orderID
        )

        findNavController().navigate(action)
    }
}