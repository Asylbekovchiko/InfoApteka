package kg.sunrise.infoapteka.ui.main.home.aboutCompany.adapter

import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.adapter.BaseVH
import kg.sunrise.infoapteka.enums.AboutCompanyType
import androidx.core.content.ContextCompat.startActivity




class AboutAdapter(

): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items = ArrayList<AboutDTO>()

    inner class AboutWithDrawableVH(
        parent: ViewGroup,
        @LayoutRes layoutResId: Int
    ): BaseVH<AboutDTO.AboutDrawDTO>(parent, layoutResId) {

        private val tvTitle: TextView = itemView.findViewById(R.id.tv_content)
        private val ivIcon: ImageView = itemView.findViewById(R.id.iv_image)

        override fun bind(item: AboutDTO.AboutDrawDTO) {
            item.apply {
                tvTitle.text = description
                ivIcon.setImageResource(drawableRes)

                itemView.setOnClickListener {
                    when (type) {
                        AboutCompanyType.PHONE -> {
                            val phoneIntent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:${item.description.replace("/", "")}")
                            }
                            startActivity(itemView.context, phoneIntent, null)
                        }
                        AboutCompanyType.EMAIL -> {
                            val intent = Intent(Intent.ACTION_SENDTO)
                            intent.data = Uri.parse("mailto:")

                            intent.putExtra(Intent.EXTRA_EMAIL, item.description)
                            try {
                                startActivity(itemView.context, intent, null)
                            } catch (e: Exception) {
                            }
                        }
                        else -> { /* nothing */}
                    }
                }
            }
        }
    }

    inner class AboutWithURLVH(
        parent: ViewGroup,
        @LayoutRes layoutResId: Int
    ): BaseVH<AboutDTO.AboutUrlDTO>(parent, layoutResId) {

        private val tvTitle: TextView = itemView.findViewById(R.id.tv_content)
        private val ivIcon: ImageView = itemView.findViewById(R.id.iv_image)

        override fun bind(item: AboutDTO.AboutUrlDTO) {
            item.apply {
                tvTitle.text = description
                Glide.with(itemView).load(imageURL).into(ivIcon)

                itemView.setOnClickListener {
                    val uri = Uri.parse(link)
                    startActivity(itemView.context,Intent(Intent.ACTION_VIEW, uri), null)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> AboutWithDrawableVH(parent, R.layout.rv_item_about_company)
            1 -> AboutWithURLVH(parent, R.layout.rv_item_about_company)
            else -> throw Throwable("1435: Error creating View Holder")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (holder) {
            is AboutWithDrawableVH -> holder.bind(item as AboutDTO.AboutDrawDTO)
            is AboutWithURLVH -> holder.bind(item as AboutDTO.AboutUrlDTO)
        }
    }

    override fun getItemViewType(position: Int) = when (items[position]) {
        is AboutDTO.AboutDrawDTO -> 0
        is AboutDTO.AboutUrlDTO -> 1
    }

    override fun getItemCount() = items.size

    fun addData(items : ArrayList<AboutDTO>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun setData(items : ArrayList<AboutDTO>) {
        this.items = items
        notifyDataSetChanged()
    }
}

sealed class AboutDTO(
    val description: String
) {
    class AboutDrawDTO(
        description: String,
        @DrawableRes val drawableRes: Int,
        val type: AboutCompanyType
    ): AboutDTO(description)

    class AboutUrlDTO(
        description: String,
        val imageURL: String?,
        val link: String
    ): AboutDTO(description)
}