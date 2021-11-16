package kg.sunrise.infoapteka.ui.shared.helpFragment.adapter

import android.view.ViewGroup
import android.widget.TextView
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.adapter.BaseAdapter
import kg.sunrise.infoapteka.base.adapter.BaseVH
import kg.sunrise.infoapteka.networking.models.response.HelpInfoResponse
import kg.sunrise.infoapteka.utils.extensions.setHtml

class HelpAdapter : BaseAdapter<HelpInfoResponse, HelpAdapter.HelpVH>() {

    inner class HelpVH(parent: ViewGroup, viewType: Int) : BaseVH<HelpInfoResponse>(parent, viewType) {

        private val tvTitle: TextView =
            itemView.findViewById(R.id.tv_title)

        private val tvDescription: TextView =
            itemView.findViewById(R.id.tv_description)

        override fun bind(item: HelpInfoResponse) {
            tvTitle.text = item.question
            tvDescription.setHtml(item.answer)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HelpVH {
        return HelpVH(parent, R.layout.rv_item_help_queations)
    }
}