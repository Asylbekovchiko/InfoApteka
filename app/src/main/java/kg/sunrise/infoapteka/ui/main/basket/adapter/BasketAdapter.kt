package kg.sunrise.infoapteka.ui.main.basket.adapter


import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.google.android.material.card.MaterialCardView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.adapter.BaseAdapter
import kg.sunrise.infoapteka.base.adapter.BaseVH
import kg.sunrise.infoapteka.databinding.RvBasketItemBinding
import kg.sunrise.infoapteka.networking.models.response.CartItem
import kg.sunrise.infoapteka.utils.extensions.setImageWithPlaceHolder


class BasketListAdapter(private val delegate: BasketDelegate) :
    ListAdapter<CartItem, BasketListAdapter.BasketVH>(BasketDiffUtil()) {

    class BasketDiffUtil : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem == newItem
        }

    }

    inner class BasketVH(parent : ViewGroup , @LayoutRes id : Int) :
        BaseVH<CartItem>(parent, id) {
        val tvTitle = itemView.findViewById<MaterialTextView>(R.id.tv_title)
        val tvOwner = itemView.findViewById<MaterialTextView>(R.id.tv_owner)
        val ivLogo = itemView.findViewById<ShapeableImageView>(R.id.iv_basket)
        val ivDelete = itemView.findViewById<ConstraintLayout>(R.id.cl_delete)
        val cvPlus = itemView.findViewById<MaterialCardView>(R.id.card_plus)
        val cvFavoutite = itemView.findViewById<MaterialCardView>(R.id.card_favourite)
        val ivFavourite = itemView.findViewById<ImageView>(R.id.iv_favorite_basket)
        val cvMinus = itemView.findViewById<MaterialCardView>(R.id.card_minus)
        val tvCount = itemView.findViewById<MaterialTextView>(R.id.tv_count_item)
        val tvPrize = itemView.findViewById<MaterialTextView>(R.id.tv_price)
        override fun bind(item: CartItem) {
            setupUI(item)
            initClickListeners(absoluteAdapterPosition, item)
        }

        private fun setupUI(item: CartItem) {
            tvTitle.text = item.drug.name ?: ""
            tvOwner.text = item.drug.owner.firstName ?: ""
            tvPrize.text =
                itemView.context.getString(R.string.Items_total_value, item.drug.price)
            setImageWithPlaceHolder(item.drug.imageURL.toString(), ivLogo, R.drawable.ic_item_placeholder)
            tvCount.text = item.quantity.toString() ?: ""
            if (item.drug.isFavourite)
                ivFavourite.setImageResource(R.drawable.ic_card_heart_checked)
            else
                ivFavourite.setImageResource(R.drawable.ic_card_heart_unchecked)
        }

        private fun initClickListeners(position: Int, item: CartItem) {
            cvPlus.setOnClickListener {
                delegate.onIncreaseItemPosition(position)
            }
            cvFavoutite.setOnClickListener {
                if (item.drug.isFavourite) {
                    ivFavourite.setImageResource(
                        R.drawable.ic_card_heart_unchecked
                    )
                } else {
                    ivFavourite.setImageResource(R.drawable.ic_card_heart_checked)
                }
                delegate.onFavouriteClickListener(position, item.drug.isFavourite)
            }
            cvMinus.setOnClickListener {
                if (tvCount.text.toString().toInt() != 1)
                    delegate.onDecreaseItemPosition(position)
            }
            ivDelete.setOnClickListener {
                delegate.onDeleteItemClickListener(position)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketVH {
        return BasketVH(parent, R.layout.rv_basket_item)
    }

    override fun onBindViewHolder(holder: BasketVH, position: Int) {
        holder.bind(getItem(position))
    }

}

interface BasketDelegate {
    fun onIncreaseItemPosition(position: Int)
    fun onDecreaseItemPosition(position: Int)
    fun onDeleteItemClickListener(position: Int)
    fun onFavouriteClickListener(position: Int, isFavourite: Boolean)
}

