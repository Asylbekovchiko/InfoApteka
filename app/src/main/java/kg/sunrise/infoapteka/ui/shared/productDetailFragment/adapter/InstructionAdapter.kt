package kg.sunrise.infoapteka.ui.shared.productDetailFragment.adapter

import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.utils.widget.ImageFilterView
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.adapter.BaseAdapter
import kg.sunrise.infoapteka.base.adapter.BaseVH

class InstructionAdapter() : BaseAdapter<Instruction, InstructionAdapter.InstructionVH>() {

    inner class InstructionVH(parent: ViewGroup, viewType: Int) :
        BaseVH<Instruction>(parent, viewType) {

        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        val tvDescription: TextView = itemView.findViewById(R.id.tv_description)

        override fun bind(item: Instruction) {
            tvTitle.text = item.title
            tvDescription.text = item.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstructionVH {
        return InstructionVH(parent, R.layout.rv_item_instruction)
    }
}
