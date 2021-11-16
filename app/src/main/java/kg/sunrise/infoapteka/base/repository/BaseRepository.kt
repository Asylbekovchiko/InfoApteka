package kg.sunrise.infoapteka.base.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response
import timber.log.Timber

abstract class BaseRepository(
    protected val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun <T> makeRequest(request: suspend () -> Response<T>?): Response<T>? =
        try {
            withContext(defaultDispatcher) {
                request()
            }
        } catch (e: Exception) {
            Timber.e(e.message)
            null
        }
}