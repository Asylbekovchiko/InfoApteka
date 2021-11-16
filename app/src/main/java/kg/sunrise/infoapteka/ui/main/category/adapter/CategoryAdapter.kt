package kg.sunrise.infoapteka.ui.main.category.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.adapter.BaseAdapter
import kg.sunrise.infoapteka.base.adapter.BaseVH
import kg.sunrise.infoapteka.networking.models.response.CategoryItemResponse
import kg.sunrise.infoapteka.utils.extensions.gone
import kg.sunrise.infoapteka.utils.extensions.visible

class CategoryAdapter(
    val delegate: CategoryAdapterDelegate
): BaseAdapter<CategoryItemResponse, CategoryAdapter.CategoryVH>() {

    inner class CategoryVH(parent: ViewGroup, viewType: Int)
        : BaseVH<CategoryItemResponse>(parent, viewType) {

        private val tvTitle: TextView = itemView.findViewById(R.id.tv_content)
        private val ivIcon: ImageView = itemView.findViewById(R.id.iv_icon)
        private val ivArrow: ImageView = itemView.findViewById(R.id.iv_arrow)

        override fun bind(item: CategoryItemResponse) {
            itemView.setOnClickListener {
                delegate.onCategoryClick(item.id)
            }

            if (item.logo != null) {
                ivIcon.visible()
                Glide.with(itemView).load(item.logo).into(ivIcon)
            } else {
                ivIcon.gone()
            }

            ivArrow.visibility = if (item.items.isNullOrEmpty()) View.GONE else View.VISIBLE

            tvTitle.text = item.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryVH {
        return CategoryVH(parent, R.layout.rv_item_category)
    }
}

interface CategoryAdapterDelegate {

    fun onCategoryClick(categoryID: Int)
}