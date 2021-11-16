package kg.sunrise.infoapteka.ui.main.home.adapter

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.adapter.BaseAdapter
import kg.sunrise.infoapteka.base.adapter.BaseVH

class MainCardAdapter(
    val delegate: MainCardAdapterDelegate
): BaseAdapter<MainCardDTO, MainCardAdapter.MainCardVH>() {

    inner class MainCardVH(parent: ViewGroup, viewType: Int)
        : BaseVH<MainCardDTO>(parent, viewType) {

        val ivIcon: ImageView = itemView.findViewById(R.id.iv_icon)
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        val tvContent: TextView = itemView.findViewById(R.id.tv_content)

        override fun bind(item: MainCardDTO) {
            ivIcon.setImageResource(item.drawableRes)
            tvTitle.setText(item.title)
            tvContent.setText(item.content)

            itemView.setOnClickListener {
                delegate.onCardClick(absoluteAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainCardVH {
        return MainCardVH(parent, R.layout.rv_item_main_card)
    }
}

interface MainCardAdapterDelegate {

    fun onCardClick(position: Int)
}

data class MainCardDTO(
    @DrawableRes val drawableRes: Int,
    @StringRes val title: Int,
    @StringRes val content: Int
)