package kg.sunrise.infoapteka.ui.main.menu.ordersHistory.adapter

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.adapter.BaseVH

class ProductHistoryAdapter
    : ListAdapter<ProductHistoryDTO, ProductHistoryAdapter.ProductHistoryVH>(ProductHistoryAdapter.ProductHistoryDiffCallback()) {

    var items = arrayListOf<ProductHistoryDTO>()

    inner class ProductHistoryVH(parent: ViewGroup, viewType: Int): BaseVH<ProductHistoryDTO>(parent, viewType) {

        val ivImage: ImageView = itemView.findViewById(R.id.iv_image)
        val tvTitle: TextView = itemView.findViewById(R.id.tv_product_name)
        val tvPrice: TextView = itemView.findViewById(R.id.tv_price)
        val tvAmount: TextView = itemView.findViewById(R.id.tv_amount)

        override fun bind(item: ProductHistoryDTO) {
            Glide.with(itemView).load(item.drug.imageURL).placeholder(R.drawable.ic_item_placeholder).into(ivImage)

            tvTitle.text = item.drug.productName
            tvPrice.text = item.drug.productPrice.toString()
            tvAmount.text = item.productAmount.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHistoryVH {
        return ProductHistoryVH(parent, R.layout.rv_item_order_product)
    }

    override fun onBindViewHolder(holder: ProductHistoryVH, position: Int) {
        holder.bind(items[position])
    }

    private class ProductHistoryDiffCallback : DiffUtil.ItemCallback<ProductHistoryDTO>() {

        override fun areItemsTheSame(oldItem: ProductHistoryDTO, newItem: ProductHistoryDTO): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductHistoryDTO, newItem: ProductHistoryDTO): Boolean {
            return oldItem == newItem
        }
    }
}