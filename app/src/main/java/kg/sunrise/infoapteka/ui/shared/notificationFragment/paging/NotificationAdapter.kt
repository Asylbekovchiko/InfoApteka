package kg.sunrise.infoapteka.ui.shared.notificationFragment.paging

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.adapter.BaseVH
import kg.sunrise.infoapteka.base.paging.BasePagingAdapter
import kg.sunrise.infoapteka.utils.extensions.getColorCompat
import kg.sunrise.infoapteka.utils.notifications.NotificationType
import timber.log.Timber

class NotificationAdapter(
    val delegate: NotificationAdapterDelegate
) : BasePagingAdapter<NotificationResponse, NotificationAdapter.NotificationVH>(
    NOTIFICATION_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationVH {
        return NotificationVH(parent, R.layout.rv_item_notification)
    }

    fun getItemAtPosition(position: Int) = getItem(position)

    inner class NotificationVH(
        parent: ViewGroup,
        @LayoutRes id: Int
    ) : BaseVH<NotificationResponse>(parent, id) {

        val ivIcon: ImageView = itemView.findViewById(R.id.iv_notification_type)
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        val tvDate: TextView = itemView.findViewById(R.id.tv_date)
        val divider: View = itemView.findViewById(R.id.divider)

        override fun bind(item: NotificationResponse) {
            divider.visibility = if (absoluteAdapterPosition >= itemCount - 1) View.INVISIBLE else View.VISIBLE

            setBackgroundColor(item.isNew)
            setNotificationIcon(item.notificationType, !item.isNew)
            tvTitle.text = item.body.title
            tvDate.text = item.createdAt

            itemView.setOnClickListener {
                delegate.onNotificationClick(item.notificationType)
            }
        }

        private fun setBackgroundColor(isNew: Boolean) {
            val colorRes = if (isNew) R.color.light_F8FBFF else R.color.white
            itemView.setBackgroundColor(itemView.context.getColorCompat(colorRes))
        }

        private fun setNotificationIcon(type: String, isRead: Boolean) {
            val drawableRes: Int = when (type.uppercase()) {
                    NotificationType.ORDER_PROCESSED.name -> {
                            if (isRead) R.drawable.ic_notification_info_read
                            else R.drawable.ic_notification_info_read
                    }
                    NotificationType.DRUG_REJECTED.name, NotificationType.ACCOUNT_REJECTED.name ->{
                            if (isRead) R.drawable.ic_notification_error_read
                            else R.drawable.ic_notification_error_unread
                    }
                    NotificationType.ORDER_DELIVERED.name ->{
                            if (isRead) R.drawable.ic_notification_info_unread
                            else R.drawable.ic_notification_info_unread
                    }
                    NotificationType.DRUG_APPROVED.name, NotificationType.ACCOUNT_APPROVED.name ->{
                            if (isRead) R.drawable.ic_notification_success_read
                            else R.drawable.ic_notification_success_unread
                    }
                    else -> { 0 }
                }

            ivIcon.setImageResource(drawableRes)
        }
    }

    companion object {
        private val NOTIFICATION_COMPARATOR = object : DiffUtil.ItemCallback<NotificationResponse>() {

            override fun areItemsTheSame(
                oldItem: NotificationResponse,
                newItem: NotificationResponse
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: NotificationResponse,
                newItem: NotificationResponse
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}

data class NotificationResponse(
    @SerializedName("notice_type")
    val notificationType: String,
    val title: String,
    val id: Int,
    @SerializedName("is_viewed")
    var isNew: Boolean,
    @SerializedName("body")
    val body: NotificationBody,
    @SerializedName("is_active")
    val isActive: String,
    @SerializedName("created_at")
    val createdAt: String
)

data class NotificationBody(
    @SerializedName("object_id")
    val objectId: Int,
    val title: String,
    val description: String
)

data class NotificationsResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: Int?,
    @SerializedName("previous")
    val previous: Int?,
    @SerializedName("results")
    val notifications: ArrayList<NotificationResponse>
)

interface NotificationAdapterDelegate {

    fun onNotificationClick(type: String)
}