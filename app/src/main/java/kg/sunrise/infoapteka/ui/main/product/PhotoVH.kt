package kg.sunrise.infoapteka.ui.main.product

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kg.sunrise.infoapteka.databinding.CvItemSertificateBinding

class PhotoVH(
    private val binding: CvItemSertificateBinding,
    private val listener: PhotoDelegate
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Photo) = with(binding) {
        tvTitle.text = item.name
        tvSize.text = item.size
        ivCloseRed.setOnClickListener {
            listener.onDeleteClick(item)
        }
        ivSertificateLogo.setOnClickListener {
            listener.onItemClick(item)
        }
        setImage(item)
    }

    private fun setImage(
        item: Photo
    ) = with(binding.ivSertificateLogo) {
        Glide.with(this)
            .load(
                when (item) {
                    is PhotoUri -> item.imageUri
                    is PhotoUrl -> item.imageUrl
                }
            )
            .into(this)
    }
}