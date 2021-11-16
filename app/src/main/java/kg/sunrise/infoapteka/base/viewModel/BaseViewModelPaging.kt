package kg.sunrise.infoapteka.base.viewModel

import androidx.paging.PagingData
import kg.sunrise.infoapteka.utils.network.State
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

abstract class BaseViewModelPaging<Item: Any> : BaseViewModel() {

    abstract var cashedData: Flow<PagingData<Item>>?

    open fun clearPaging() {
        cashedData = null
    }

    protected suspend fun<T> getResponsePaging(function: suspend () -> Response<T>?): Response<T>? {
        if (!hasConnection()) return null

        _state.value = State.LoadingState(true)

        val response = function()

        _state.value = State.LoadingState(false)

        return response
    }

    open fun clearDataIfNeed() {
        if (!hasInternet) {
            clearPaging()
        }
    }
}