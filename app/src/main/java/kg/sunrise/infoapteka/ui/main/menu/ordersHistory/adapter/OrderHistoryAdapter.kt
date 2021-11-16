package kg.sunrise.infoapteka.ui.main.menu.ordersHistory.adapter

import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.gson.annotations.SerializedName
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.adapter.BaseVH
import kg.sunrise.infoapteka.enums.OrderHistoryType
import kg.sunrise.infoapteka.utils.extensions.getColorCompat
import kg.sunrise.infoapteka.utils.extensions.gone
import kg.sunrise.infoapteka.utils.extensions.visible
import kg.sunrise.infoapteka.utils.parsers.DateTimeParser
import kg.sunrise.infoapteka.utils.parsers.DateTimePattern
import java.util.*

interface OrderHistoryAdapterDelegate {

    fun scrollToPosition(position: Int)
    fun setExpanded(isExpanded: Boolean, position: Int)
    fun onReorderClick(orderId: Int, itemsCount: Int, totalSum: Int, orderID: String)
}

class OrderHistoryAdapter(
    val delegate: OrderHistoryAdapterDelegate
): ListAdapter<OrderHistoryDTO, OrderHistoryAdapter.OrderHistoryVH>(OrderHistoryDiffCallback()) {

    var items: ArrayList<OrderHistoryDTO> = arrayListOf()

    fun getItemByPosition(position: Int): OrderHistoryDTO {
        return items[position]
    }

    inner class OrderHistoryVH(parent: ViewGroup, viewType: Int) :
        BaseVH<OrderHistoryDTO>(parent, viewType) {

        private val motionToggle: MotionLayout = itemView.findViewById(R.id.motion_toggle)
        private val tvOrderNumber: TextView = itemView.findViewById(R.id.tv_order_number)
        private val tvOrderDate: TextView = itemView.findViewById(R.id.tv_order_date)
        private val tvOrderTime: TextView = itemView.findViewById(R.id.tv_order_time)
        private val tvStatus: TextView = itemView.findViewById(R.id.tv_status)
        private val tvProductCount: TextView = itemView.findViewById(R.id.tv_product_count)
        private val tvTotalAmount: TextView = itemView.findViewById(R.id.tv_total_amount)
        private val btnExpand: MaterialButton = itemView.findViewById(R.id.btn_expand)
        private val btnCollapse: MaterialButton = itemView.findViewById(R.id.btn_collapse)
        private val btnReorder: MaterialButton = itemView.findViewById(R.id.btn_reorder)

        private val rvItems: RecyclerView = itemView.findViewById(R.id.rv_items)

        override fun bind(item: OrderHistoryDTO) {
            if (item.isExpanded) {
                motionToggle.transitionToState(motionToggle.endState)
                btnExpand.gone()
                btnCollapse.visible()
            } else {
                motionToggle.transitionToState(motionToggle.startState)
                btnExpand.visible()
                btnCollapse.gone()
            }

            if (item.products.count() == 1) {
                btnExpand.gone()
                btnCollapse.gone()
            }

            val productsAdapter = ProductHistoryAdapter()

            rvItems.adapter = productsAdapter
            productsAdapter.items = item.products
            productsAdapter.submitList(productsAdapter.items)

            tvOrderNumber.text =
                itemView.resources.getString(R.string.Order_number, item.orderNumber)
            tvOrderDate.text = DateTimeParser.parseFromISO(item.orderDate, DateTimePattern.dd_MMMM_yyyy)
            tvOrderTime.text = DateTimeParser.parseFromISO(item.orderDate, DateTimePattern.HH_mm)

            val productCount = item.totalItems
            val productPrice = item.totalSum

            tvProductCount.text = productCount.toString()
            tvTotalAmount.text = productPrice.toString()

            setupStatus(item)

            btnExpand.setOnClickListener {
                delegate.scrollToPosition(absoluteAdapterPosition)
                delegate.setExpanded(!item.isExpanded, absoluteAdapterPosition)

                btnExpand.gone()
                btnCollapse.visible()

                motionToggle.transitionToEnd()
            }

            btnCollapse.setOnClickListener {
                delegate.scrollToPosition(absoluteAdapterPosition)
                delegate.setExpanded(!item.isExpanded, absoluteAdapterPosition)

                btnExpand.visible()
                btnCollapse.gone()

                motionToggle.transitionToStart()
            }
        }

        private fun setupStatus(item: OrderHistoryDTO) {
            tvStatus.setText(
                if (item.orderStatus == OrderHistoryType.ACCEPTED) R.string.Product_ordered
                else R.string.Product_delivered
            )

            tvStatus.setTextColor(
                itemView.context.getColorCompat(
                    if (item.orderStatus == OrderHistoryType.COMPLETED) R.color.green
                    else R.color.blue
                )
            )

            setupReorder(item)
        }

        private fun setupReorder(item: OrderHistoryDTO) {
            // todo check to status and show reorder if ok
            btnReorder.visible()

            btnReorder.setOnClickListener {
                delegate.onReorderClick(item.id.toIntOrNull() ?: -1, item.totalItems, item.totalSum, item.orderNumber.toString())
            }
        }
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryVH {
        return OrderHistoryVH(parent, R.layout.rv_item_order_history)
    }

    override fun onBindViewHolder(holder: OrderHistoryVH, position: Int) {
        holder.bind(items[position])
    }

    fun setData(data: ArrayList<OrderHistoryDTO>) {
        items = data
        submitList(items)
    }

    private class OrderHistoryDiffCallback : DiffUtil.ItemCallback<OrderHistoryDTO>() {

        override fun areItemsTheSame(oldItem: OrderHistoryDTO, newItem: OrderHistoryDTO): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: OrderHistoryDTO, newItem: OrderHistoryDTO): Boolean {
            return oldItem == newItem
        }
    }
}

data class OrderHistoryDTO(
    @SerializedName("order_id")
    val orderNumber: Int,
    @SerializedName("created_at")
    val orderDate: String,
    val orderStatus: OrderHistoryType,
    @SerializedName("get_order_total_items")
    val totalItems: Int,
    @SerializedName("get_order_total")
    val totalSum: Int,
    @SerializedName("order_items")
    val products: ArrayList<ProductHistoryDTO>
) {
    var isExpanded = true
    var id: String = UUID.randomUUID().toString()
}

data class ProductHistoryDTO (
    val drug: OrderDrug,
    @SerializedName("quantity")
    val productAmount: Int
) {
    var id: String = UUID.randomUUID().toString()
}

data class OrderDrug(
    @SerializedName("name")
    val productName: String,
    @SerializedName("price")
    val productPrice: Int,
    @SerializedName("image")
    val imageURL: String
)