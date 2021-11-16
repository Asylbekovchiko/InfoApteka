package kg.sunrise.infoapteka.ui.main.product

import androidx.recyclerview.widget.DiffUtil

class PhotoCallback : DiffUtil.ItemCallback<Photo>() {

    override fun areItemsTheSame(
        oldItem: Photo, newItem: Photo
    ) = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: Photo, newItem: Photo
    ) = oldItem == newItem

}