package kg.sunrise.infoapteka.ui.main.home.farmAdresses.fragments.farmAdresses.detailFarm.adapter

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.textview.MaterialTextView
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.adapter.BaseAdapter
import kg.sunrise.infoapteka.base.adapter.BaseVH

class PhoneFarmAdapter(private val delegate : OnPhoneClickInterface) : BaseAdapter<String, PhoneFarmAdapter.PhoneFarmVH>() {

    inner class PhoneFarmVH(parent: ViewGroup, @LayoutRes id: Int) : BaseVH<String>(parent, id) {
        private val tvPhone = itemView.findViewById<MaterialTextView>(R.id.tv_phone_farm)
        private val clContent = itemView.findViewById<ConstraintLayout>(R.id.cl_content)
        override fun bind(item: String) {
            tvPhone.text = item
            clContent.setOnClickListener {
                delegate.phoneCardClickListener(absoluteAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhoneFarmVH {
        return PhoneFarmVH(parent , R.layout.rv_phone_farm_item)

    }
}

fun interface OnPhoneClickInterface {
    fun phoneCardClickListener(position : Int)
}