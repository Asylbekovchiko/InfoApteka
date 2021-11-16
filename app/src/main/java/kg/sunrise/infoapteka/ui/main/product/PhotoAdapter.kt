package kg.sunrise.infoapteka.ui.main.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import kg.sunrise.infoapteka.databinding.CvItemSertificateBinding


class PhotoAdapter(
    private val photoDelegate: PhotoDelegate
) : ListAdapter<Photo, PhotoVH>(PhotoCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ) = PhotoVH(
        CvItemSertificateBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ),
        photoDelegate
    )

    override fun onBindViewHolder(holder: PhotoVH, position: Int) {
        holder.bind(getItem(position))
    }

}