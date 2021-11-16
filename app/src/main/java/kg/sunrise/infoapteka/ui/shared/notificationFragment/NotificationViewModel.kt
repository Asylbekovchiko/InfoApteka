package kg.sunrise.infoapteka.ui.shared.notificationFragment

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kg.sunrise.infoapteka.base.viewModel.BaseViewModelPaging
import kg.sunrise.infoapteka.networking.repositories.NotificationRepository
import kg.sunrise.infoapteka.ui.shared.notificationFragment.paging.NotificationResponse
import kg.sunrise.infoapteka.ui.shared.notificationFragment.paging.NotificationsResponse
import kg.sunrise.infoapteka.utils.network.State
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class NotificationViewModel(
    private val notificationRepository: NotificationRepository
) : BaseViewModelPaging<NotificationResponse>() {

    override var cashedData: Flow<PagingData<NotificationResponse>>? = null

    fun getNotificationPaging(): Flow<PagingData<NotificationResponse>> {
        val lastResult = cashedData

        if (lastResult != null)
            return lastResult

        val result = notificationRepository.getNotificationsByPage { page ->
            getNotifications(page)
        }.cachedIn(viewModelScope)

        cashedData = result

        return result
    }

    private suspend fun getNotifications(
        page: Int
    ): Response<NotificationsResponse>? {
        return getResponsePaging {
            val response = notificationRepository.getNotifications(page)

            if (!response.hasBody()) return@getResponsePaging null

            if (response!!.isSuccess()) {
                _state.value = State.SuccessState.NoItemState
            }

            return@getResponsePaging response
        }
    }
}