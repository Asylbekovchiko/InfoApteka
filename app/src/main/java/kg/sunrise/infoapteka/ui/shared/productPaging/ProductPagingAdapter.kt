package kg.sunrise.infoapteka.ui.shared.productPaging

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.google.android.material.imageview.ShapeableImageView
import com.google.gson.annotations.SerializedName
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.adapter.BaseVH
import kg.sunrise.infoapteka.base.paging.BasePagingAdapter
import kg.sunrise.infoapteka.enums.ProductShowType
import kg.sunrise.infoapteka.networking.models.response.ProductResponse
import kg.sunrise.infoapteka.utils.extensions.gone
import kg.sunrise.infoapteka.utils.extensions.visible

class ProductPagingAdapter(
    val delegate: ProductAdapterDelegate,
    var type: ProductShowType = ProductShowType.GRID
): BasePagingAdapter<ProductResponse, ProductPagingAdapter.ProductVH>(PRODUCT_COMPARATOR) {

    fun getItemAtPosition(position: Int) = getItem(position)

    inner class ProductVH(parent: ViewGroup, viewType: Int)
        : BaseVH<ProductResponse>(parent, viewType) {

        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        val tvOwner: TextView = itemView.findViewById(R.id.tv_owner)
        val tvPrice: TextView = itemView.findViewById(R.id.tv_price)
        val tvNotAvailable: TextView = itemView.findViewById(R.id.tv_not_available)
        val ivImage: ShapeableImageView = itemView.findViewById(R.id.iv_image)
        val cardFavorite: MaterialCardView = itemView.findViewById(R.id.card_favourite)
        val cardBasket: MaterialCardView = itemView.findViewById(R.id.card_basket)
        val ivFavorite: ImageView = itemView.findViewById(R.id.iv_favourite)
        val ivBasket: ImageView = itemView.findViewById(R.id.iv_basket)

        override fun bind(item: ProductResponse) {
            setupUI(item)
            initListeners()

            itemView.setOnClickListener {
                delegate.onProductClick(absoluteAdapterPosition)
            }

            tvNotAvailable.setOnClickListener {
                delegate.onIsNotAvailableClick(item)
            }
        }

        private fun setupUI(item: ProductResponse) {
            Glide.with(itemView).load(item.imageURL).placeholder(R.drawable.ic_item_placeholder).into(ivImage)

            tvTitle.text = item.name
            tvOwner.text = item.owner.firstName
            tvPrice.text = item.price.toString()

            tvNotAvailable.visibility = if (item.isAvailable) View.GONE else View.VISIBLE
            ivFavorite.setImageResource(
                if (item.isFavourite) R.drawable.ic_card_heart_checked
                else R.drawable.ic_card_heart_unchecked
            )
            if (item.isOwner || item.isAvailable.not()) {
                cardBasket.gone()
            } else {
                cardBasket.visible()
                ivBasket.setImageResource(
                    if (item.isInBasket) R.drawable.ic_card_basket_purchased
                    else R.drawable.ic_card_basket
                )
            }
        }

        private fun initListeners() {
            cardFavorite.setOnClickListener {
                delegate.onFavoriteClick(absoluteAdapterPosition)
            }

            cardBasket.setOnClickListener {
                delegate.onBasketClick(absoluteAdapterPosition)
            }
        }
    }

    override fun getItemViewType(position: Int) = when (type) {
        ProductShowType.GRID -> R.layout.rv_item_grid_product
        ProductShowType.BLOCK -> R.layout.rv_item_linear_product
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductVH {
        return when (viewType) {
            R.layout.rv_item_grid_product -> ProductVH(parent, R.layout.rv_item_grid_product)
            R.layout.rv_item_linear_product -> ProductVH(parent, R.layout.rv_item_linear_product)
            else -> throw Throwable("1435: Error creating View Holder")
        }
    }

    fun updateShowType(type: ProductShowType) {
        this.type = type
    }

    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<ProductResponse>() {

            override fun areItemsTheSame(
                oldItem: ProductResponse,
                newItem: ProductResponse
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ProductResponse,
                newItem: ProductResponse
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}

interface ProductAdapterDelegate {

    fun onProductClick(position: Int)

    fun onFavoriteClick(position: Int)

    fun onBasketClick(position: Int)

    fun onIsNotAvailableClick(response: ProductResponse)
}

