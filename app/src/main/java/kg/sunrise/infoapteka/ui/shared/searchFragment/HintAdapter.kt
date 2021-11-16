package kg.sunrise.infoapteka.ui.shared.searchFragment

import android.view.ViewGroup
import android.widget.TextView
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.adapter.BaseAdapter
import kg.sunrise.infoapteka.base.adapter.BaseVH

class HintAdapter(
    val delegate: HintAdapterDelegate
): BaseAdapter<String, HintAdapter.HintVH>() {

    inner class HintVH(parent: ViewGroup, id: Int): BaseVH<String>(parent, id) {

        private val tvHint: TextView = itemView.findViewById<TextView>(R.id.tv_hint)

        override fun bind(item: String) {
            itemView.setOnClickListener {
                delegate.onHintClicked(item)
            }
            tvHint.text = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HintVH {
        return HintVH(parent, R.layout.rv_item_hint)
    }
}

interface HintAdapterDelegate {

    fun onHintClicked(productName: String)
}