package kg.sunrise.infoapteka.base.adapter

import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<Item, VH: BaseVH<Item>> : RecyclerView.Adapter<VH>() {

    protected val items = ArrayList<Item>()

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    open fun addData(items : ArrayList<Item>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    open fun getAllItems() : ArrayList<Item> = items

    open fun setData(items : ArrayList<Item>) {
        this.items.clear()
        addData(items)
    }
}

