package kg.sunrise.infoapteka.base.paging

import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import kg.sunrise.infoapteka.base.adapter.BaseVH

abstract class BasePagingAdapter<Item : Any, VH : BaseVH<Item>>(diffCallback : DiffUtil.ItemCallback<Item>)
    : PagingDataAdapter<Item, VH>(diffCallback) {
    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)

        item?.let {
            holder.bind(it)
        }
    }
}