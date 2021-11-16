package kg.sunrise.infoapteka.ui.main.menu.adapter

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.adapter.BaseVH
import kg.sunrise.infoapteka.enums.ModerationType
import kg.sunrise.infoapteka.utils.extensions.getColorCompat
import kg.sunrise.infoapteka.utils.extensions.gone
import kg.sunrise.infoapteka.utils.extensions.visible

class MenuAdapter(
    val delegate: MenuAdapterDelegate
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items = ArrayList<MenuOption>()

    fun addData(items : ArrayList<MenuOption>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun setData(items : ArrayList<MenuOption>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int) = when (items[position]) {
        is MenuOption.SimpleItem -> R.layout.rv_item_menu
        is MenuOption.ProfileItem -> R.layout.rv_item_menu_profile
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.rv_item_menu -> MenuVH(parent, R.layout.rv_item_menu)
            R.layout.rv_item_menu_profile -> MenuProfileVH(parent, R.layout.rv_item_menu_profile)
            else -> throw Throwable("1435: Error creating View Holder")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (holder) {
            is MenuVH -> holder.bind(item as MenuOption.SimpleItem)
            is MenuProfileVH -> holder.bind(item as MenuOption.ProfileItem)
        }
    }

    fun fillModerationDescription(status: String) {
        val firstItem = items.first()
        if (firstItem is MenuOption.ProfileItem) {
            val moderationType = when (status) {
                "rejected" -> ModerationType.DISMISSED
                "waiting" -> ModerationType.IN_PROGRESS
                "activated" -> ModerationType.ALLOWED
                else -> ModerationType.EMPTY
            }
            firstItem.moderationType = moderationType
            notifyItemChanged(0)
        }
    }

    inner class MenuVH(
        parent: ViewGroup,
        @LayoutRes layoutResId: Int
    ): BaseVH<MenuOption.SimpleItem>(parent, layoutResId) {

        private val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        private val ivIcon: ImageView = itemView.findViewById(R.id.iv_icon)

        override fun bind(item: MenuOption.SimpleItem) {
            item.apply {
                tvTitle.setText(title)

                Glide.with(itemView).load(icon).into(ivIcon)

                tvTitle.setTextColor(itemView.context.getColorCompat(
                    if (type == MenuType.EXIT) R.color.secondary_red else R.color.black
                ))

                itemView.setOnClickListener {
                    delegate.onItemClickListener(type)
                }
            }
        }
    }

    inner class MenuProfileVH(
        parent: ViewGroup,
        @LayoutRes layoutResId: Int
    ): BaseVH<MenuOption.ProfileItem>(parent, layoutResId) {

        private val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        private val tvDescription: TextView = itemView.findViewById(R.id.tv_description)
        private val ivIcon: ImageView = itemView.findViewById(R.id.iv_icon)

        override fun bind(item: MenuOption.ProfileItem) {
            item.apply {
                tvTitle.setText(title)

                Glide.with(itemView).load(icon).into(ivIcon)

                when (item.moderationType) {
                    ModerationType.ALLOWED -> {
                        setupProfileModeration(title, R.string.moderation_passed, R.color.green)
                    }
                    ModerationType.DISMISSED -> {
                        setupProfileModeration(title, R.string.moderation_denied, R.color.secondary_red)
                    }
                    ModerationType.IN_PROGRESS -> {
                        setupProfileModeration(title, R.string.moderation_in_progress, R.color.orange)
                    }
                    ModerationType.EMPTY -> {
                        tvDescription.gone()
                    }
                }

                itemView.setOnClickListener {
                    delegate.onItemClickListener(type)
                }
            }
        }

        private fun setupProfileModeration(@StringRes titleRes: Int, @StringRes textRes: Int, @ColorRes colorRes: Int) {
            tvTitle.text = itemView.resources.getString(titleRes) + " - "
            tvDescription.visible()
            tvDescription.setText(textRes)
            tvDescription.setTextColor(itemView.context.getColorCompat(colorRes))
        }
    }
}

sealed class MenuOption(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val type: MenuType
) {
    class SimpleItem(
        @StringRes title: Int,
        @DrawableRes icon: Int,
        type: MenuType
    ) : MenuOption(title, icon, type)

    class ProfileItem(
        @StringRes title: Int,
        @DrawableRes icon: Int,
        type: MenuType,
        var moderationType: ModerationType
    ) : MenuOption(title, icon, type)
}

enum class MenuType {
    ENTER,
    PROFILE,
    ORDERS_HISTORY,
    ABOUT_COMPANY,
    HELP,
    RULES,
    MY_PRODUCTS,
    EXIT
}

interface MenuAdapterDelegate {
    fun onItemClickListener(type: MenuType)
}
