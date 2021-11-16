package kg.sunrise.infoapteka.ui.shared.notificationFragment.paging

import kg.sunrise.infoapteka.base.paging.BasePagingSource
import retrofit2.Response

class NotificationPagingSource(
    getNotificationsCallback: suspend (Int) -> Response<NotificationsResponse>?
) : BasePagingSource<NotificationsResponse, NotificationResponse>(getNotificationsCallback) {

    override fun getNextKey(responseBody: NotificationsResponse): Int? {
        return responseBody.next
    }

    override fun getResponseItem(responseBody: NotificationsResponse): ArrayList<NotificationResponse> {
        return responseBody.notifications
    }
}