package kg.sunrise.infoapteka.ui.shared.productDetailFragment.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.google.android.material.imageview.ShapeableImageView
import com.google.gson.annotations.SerializedName
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.adapter.BaseAdapter
import kg.sunrise.infoapteka.base.adapter.BaseVH
import kg.sunrise.infoapteka.ui.shared.productPaging.ProductAdapterDelegate
import kg.sunrise.infoapteka.networking.models.response.ProductResponse
import kg.sunrise.infoapteka.utils.extensions.gone

class ProductAdapter(
    val delegate: ProductAdapterDelegate,
): BaseAdapter<ProductResponse, ProductAdapter.ProductVH>() {

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
        }

        private fun setupUI(item: ProductResponse) {
            Glide.with(itemView).load(item.imageURL).placeholder(R.drawable.ic_item_placeholder).into(ivImage)

            tvTitle.text = item.name
            tvOwner.text = item.owner.firstName
            tvPrice.text = item.price.toString() + " сом"

            tvNotAvailable.visibility = if (item.isAvailable) View.GONE else View.VISIBLE
            ivFavorite.setImageResource(
                if (item.isFavourite) R.drawable.ic_card_heart_checked
                else R.drawable.ic_card_heart_unchecked
            )
            if (item.isOwner) {
                ivBasket.gone()
            } else {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductVH {
        return ProductVH(parent, R.layout.rv_item_grid_product_border)
    }

    fun getItem(position: Int) = items[position]
}

data class Instruction(
    @SerializedName("instruction")
    val title: String,
    @SerializedName("description")
    val description: String
)