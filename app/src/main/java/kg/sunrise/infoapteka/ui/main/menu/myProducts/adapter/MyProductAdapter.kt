package kg.sunrise.infoapteka.ui.main.menu.myProducts.adapter

import android.content.res.ColorStateList
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.imageview.ShapeableImageView
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.adapter.BaseVH
import kg.sunrise.infoapteka.base.paging.BasePagingAdapter
import kg.sunrise.infoapteka.enums.ModerationType
import kg.sunrise.infoapteka.utils.extensions.getColorCompat

class MyProductAdapter(val onDetailClicked: (Int) -> Unit) :
    BasePagingAdapter<MyProductDTO, MyProductAdapter.MyProductVH>(
        MY_PRODUCT_COMPARATOR
    ) {

    inner class MyProductVH(parent: ViewGroup, id: Int): BaseVH<MyProductDTO>(parent, id) {

        val ivImage: ShapeableImageView = itemView.findViewById(R.id.iv_image)
        val tvProductName: TextView = itemView.findViewById(R.id.tv_product_name)
        val tvOwner: TextView = itemView.findViewById(R.id.tv_owner)
        val tvProductPrice: TextView = itemView.findViewById(R.id.tv_product_price)
        val btnDetail: MaterialButton = itemView.findViewById(R.id.btn_detail)
        val cardModeration: MaterialCardView = itemView.findViewById(R.id.card_moderation)
        val tvModeration: TextView = itemView.findViewById(R.id.tv_moderation)
        val groupVisibility: Group = itemView.findViewById(R.id.group_visibility)

        override fun bind(item: MyProductDTO) {
            Glide.with(itemView).load(item.imageURL).placeholder(R.drawable.ic_item_placeholder)
                .into(ivImage)

            tvProductName.text = item.title
            tvOwner.text = item.owner
            tvProductPrice.text = item.price

            setModerationType(item.state)

            btnDetail.setOnClickListener {
                onDetailClicked(absoluteAdapterPosition)
            }
        }

        private fun setModerationType(type: ModerationType) {
            when (type) {
                ModerationType.IN_PROGRESS -> {
                    groupVisibility.visibility = View.VISIBLE
                    cardModeration.backgroundTintList = ColorStateList.valueOf(itemView.context.getColorCompat(R.color.orange))

                    tvModeration.setText(R.string.On_moderation)
                }
                ModerationType.DISMISSED -> {
                    groupVisibility.visibility = View.VISIBLE
                    cardModeration.backgroundTintList = ColorStateList.valueOf(itemView.context.getColorCompat(R.color.secondary_red))
                    tvModeration.setText(R.string.Dismissed)
                }
                else -> {
                    groupVisibility.visibility = View.GONE
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyProductVH {
        return MyProductVH(parent, R.layout.rv_item_my_product)
    }

    fun getItemAt(position: Int) = getItem(position)

    companion object {
        private val MY_PRODUCT_COMPARATOR =
            object : DiffUtil.ItemCallback<MyProductDTO>() {

                override fun areItemsTheSame(
                    oldItem: MyProductDTO,
                    newItem: MyProductDTO
                ) = oldItem.id == newItem.id

                override fun areContentsTheSame(
                    oldItem: MyProductDTO,
                    newItem: MyProductDTO
                ) = oldItem == newItem
            }
    }
}

data class MyProductDTO(
    val id: Int,
    val title: String,
    val owner: String,
    val imageURL: String?,
    val price: String,
    val state: ModerationType
)
