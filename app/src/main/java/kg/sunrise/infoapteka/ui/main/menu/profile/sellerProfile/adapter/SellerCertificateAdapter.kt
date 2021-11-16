    package kg.sunrise.infoapteka.ui.main.menu.profile.sellerProfile.adapter

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.adapter.BaseAdapter
import kg.sunrise.infoapteka.base.adapter.BaseVH
import kg.sunrise.infoapteka.networking.models.response.SertficateResponse
import kg.sunrise.infoapteka.utils.extensions.convertToRoundNumber
import kg.sunrise.infoapteka.utils.extensions.toMb
import kotlinx.coroutines.*

    class SellerCertificateAdapter(private val click: SellerCertificateClickListener) :
    BaseAdapter<SertficateResponse, SellerCertificateAdapter.SellerCertificateVH>() {

    inner class SellerCertificateVH(parent: ViewGroup, @LayoutRes id: Int) :
        BaseVH<SertficateResponse>(parent, id) {

        val tvTitle = itemView.findViewById<MaterialTextView>(R.id.tv_title)
        val tvDesc = itemView.findViewById<MaterialTextView>(R.id.tv_size)
        val ivLogo = itemView.findViewById<ShapeableImageView>(R.id.iv_sertificate_logo)
        val ivCancel = itemView.findViewById<ShapeableImageView>(R.id.iv_close_red)

        override fun bind(item: SertficateResponse) {
            Glide.with(ivLogo).load(item.image).into(ivLogo)
            Glide
                .with(itemView.context)
                .asBitmap()
                .load(item.image)
                .into(object : SimpleTarget<Bitmap?>() {
                    @SuppressLint("StringFormatMatches")
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap?>?
                    ) {
                        var byteCount = Int.MIN_VALUE
                        byteCount =
                            resource.allocationByteCount
                        val mb = byteCount.toMb()
                         tvDesc.text = itemView.context.getString(
                            R.string.sertificateSize,
                            convertToRoundNumber(mb)
                        )
                    }
                })
            val withPreShash = item.image.substring(item.image.lastIndexOf("/"))
            tvTitle.text = withPreShash.substring(1)
            ivLogo.setOnClickListener {
                click.onClickListener(
                    absoluteAdapterPosition,
                    type = ImageClickOnSertificate.ImageZoom
                )
            }
            ivCancel.setOnClickListener {
                click.onClickListener(
                    absoluteAdapterPosition,
                    type = ImageClickOnSertificate.RedCross
                )
            }
        }
    }

    fun deletedItemUpdate(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellerCertificateVH {
        return SellerCertificateVH(parent, R.layout.cv_item_sertificate)
    }

    fun getAt(position: Int) = items[position]

}



interface SellerCertificateClickListener {
    fun onClickListener(position: Int, type: ImageClickOnSertificate)
}

enum class ImageClickOnSertificate {
    ImageZoom,
    RedCross
}
