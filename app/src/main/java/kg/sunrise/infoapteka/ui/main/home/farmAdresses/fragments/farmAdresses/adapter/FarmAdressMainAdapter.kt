package kg.sunrise.infoapteka.ui.main.home.farmAdresses.fragments.farmAdresses.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.adapter.BaseVH
import kg.sunrise.infoapteka.base.paging.BasePagingAdapter
import kg.sunrise.infoapteka.networking.models.response.BranchAdressesResponse
import kg.sunrise.infoapteka.utils.extensions.setImageWithPlaceHolder
import kg.sunrise.infoapteka.utils.extensions.setTextColorRes
import timber.log.Timber


class FarmAdressAdapterPaging(private val delegate : ClickOnCardFarm) :
    BasePagingAdapter<BranchAdressesResponse, FarmAdressAdapterPaging.BranchVH>(FarmDiffUtil()) {

    class FarmDiffUtil : DiffUtil.ItemCallback<BranchAdressesResponse>() {
        override fun areItemsTheSame(
            oldItem: BranchAdressesResponse,
            newItem: BranchAdressesResponse
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: BranchAdressesResponse,
            newItem: BranchAdressesResponse
        ): Boolean {
            return oldItem == newItem
        }

    }

    inner class BranchVH(parent: ViewGroup, @LayoutRes id: Int) :
        BaseVH<BranchAdressesResponse>(parent, id) {
        private val clMain = itemView.findViewById<ConstraintLayout>(R.id.cl_main)
        private val tvOpen = itemView.findViewById<MaterialTextView>(R.id.tv_open)
        private val tvName = itemView.findViewById<MaterialTextView>(R.id.tv_name_farm)
        private val tvAdress = itemView.findViewById<MaterialTextView>(R.id.tv_adress_farm)
        private val ivLogo = itemView.findViewById<ShapeableImageView>(R.id.iv_adress_logo)
        private val tvDistance = itemView.findViewById<MaterialTextView>(R.id.tv_distance)
        @SuppressLint("StringFormatMatches")
        override fun bind(item: BranchAdressesResponse) {
            if(item.isOpen) {
                tvOpen.text = itemView.context.getString(R.string.farm_open)
                tvOpen.setTextColorRes(R.color.green_secondary)
            }
            else
                tvOpen.text = itemView.context.getString(R.string.farm_close)
            tvName.text = item.name ?: ""
            tvAdress.text = item.address ?: ""
            setImageWithPlaceHolder(
                item.image,
                ivLogo,
                placeHolder = R.drawable.ic_item_placeholder
            )
            tvDistance.text = itemView.context.getString(R.string.distancePattern ,item.distance ?: "-" )
            clMain.setOnClickListener {
                delegate.clickOnCard(snapshot().items[absoluteAdapterPosition].id)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FarmAdressAdapterPaging.BranchVH {
        return BranchVH(parent, R.layout.rv_item_adress)
    }

}

fun interface ClickOnCardFarm {
     fun clickOnCard(position : Int)
}


