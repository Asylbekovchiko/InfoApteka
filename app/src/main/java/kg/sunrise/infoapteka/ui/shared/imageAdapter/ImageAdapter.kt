package kg.sunrise.infoapteka.ui.shared.imageAdapter

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.adapter.BaseAdapter
import kg.sunrise.infoapteka.base.adapter.BaseVH
import kg.sunrise.infoapteka.enums.ImageRatioType

class ImageAdapter(
    val delegate: ImageAdapterDelegate,
    val mainPage: ImageRatioType
): BaseAdapter<String, ImageAdapter.MainImageVH>() {

    inner class MainImageVH(parent: ViewGroup, @LayoutRes id: Int)
        : BaseVH<String>(parent, id) {

        val image: ShapeableImageView = itemView.findViewById(R.id.iv_image)

        override fun bind(item: String) {
            Glide.with(itemView).load(item).into(image)

            itemView.setOnClickListener {
                delegate.onImageClick(absoluteAdapterPosition)
            }

            image.updateLayoutParams<ConstraintLayout.LayoutParams> {
                dimensionRatio = mainPage.ratio
                if (mainPage == ImageRatioType.MAIN_PAGE) {
                    image.shapeAppearanceModel = image.shapeAppearanceModel.toBuilder().setAllCornerSizes(itemView.resources.getDimension(R.dimen.dp_1_5x)).build()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainImageVH {
        return MainImageVH(parent, R.layout.rv_item_image)
    }

    fun getItem(position: Int) = items[position]
}

interface ImageAdapterDelegate {

    fun onImageClick(position: Int)
}