package kg.sunrise.infoapteka.networking.api

import kg.sunrise.infoapteka.ui.shared.notificationFragment.paging.NotificationsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NotificationApi {

    @GET("notification/")
    suspend fun getNotificationsPagingBy(
        @Query("page") page: Int
    ): Response<NotificationsResponse>
}