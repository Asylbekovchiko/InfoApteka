package kg.sunrise.infoapteka.networking.repositories

import androidx.paging.PagingData
import kg.sunrise.infoapteka.base.repository.BaseRepositoryPaging
import kg.sunrise.infoapteka.networking.api.NotificationApi
import kg.sunrise.infoapteka.ui.shared.notificationFragment.paging.NotificationPagingSource
import kg.sunrise.infoapteka.ui.shared.notificationFragment.paging.NotificationResponse
import kg.sunrise.infoapteka.ui.shared.notificationFragment.paging.NotificationsResponse
import kg.sunrise.infoapteka.utils.constants.DEFAULT_PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class NotificationRepository(
    val notificationApi: NotificationApi
)
    : BaseRepositoryPaging() {

    fun getNotificationsByPage(
        getNotificationsCallback: suspend (Int) -> Response<NotificationsResponse>?)
    : Flow<PagingData<NotificationResponse>> {
        return getDataByPage(
            DEFAULT_PAGE_SIZE,
            NotificationPagingSource() { page ->
                getNotificationsCallback(page)
            }
        )
    }

    suspend fun getNotifications(page: Int): Response<NotificationsResponse>? {
        return makeRequest {
            notificationApi.getNotificationsPagingBy(page)
        }
    }
}