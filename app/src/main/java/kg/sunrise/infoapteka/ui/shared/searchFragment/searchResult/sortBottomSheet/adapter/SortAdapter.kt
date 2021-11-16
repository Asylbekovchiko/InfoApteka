package kg.sunrise.infoapteka.ui.shared.searchFragment.searchResult.sortBottomSheet.adapter

import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.google.android.material.radiobutton.MaterialRadioButton
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.adapter.BaseAdapter
import kg.sunrise.infoapteka.base.adapter.BaseVH
import kg.sunrise.infoapteka.enums.SortType
import kg.sunrise.infoapteka.ui.shared.searchFragment.searchResult.RadioBtnItem

class SortAdapter(
    val delegate: SortAdapterDelegate,
): BaseAdapter<RadioBtnItem, SortAdapter.SortVH>() {

    inner class SortVH(
        parent: ViewGroup,
        @LayoutRes layoutResId: Int
    ): BaseVH<RadioBtnItem>(parent, layoutResId) {

        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        val checkButton: MaterialRadioButton = itemView.findViewById(R.id.radio_button)

        override fun bind(item: RadioBtnItem) {
            tvTitle.setText(item.title)
            checkButton.isClickable = false
            checkButton.isChecked = item.isChecked

            itemView.setOnClickListener {
                checkButton.isChecked = true
                delegate.onSortItemClick(item.type, absoluteAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SortVH {
        return SortVH(parent, R.layout.rv_item_single_choise)
    }
}

interface SortAdapterDelegate {

    fun onSortItemClick(type: SortType, position: Int)
}