package kg.sunrise.infoapteka.networking.repositories

import kg.sunrise.infoapteka.base.repository.BaseRepository
import kg.sunrise.infoapteka.networking.api.AuthApi
import kg.sunrise.infoapteka.networking.models.request.UserRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class AuthRepository(
    private val api: AuthApi
) : BaseRepository() {

    suspend fun checkUser(phoneNumber: String) = makeRequest {
        api.checkUser(phoneNumber)
    }

    suspend fun auth(phoneNumber: String) = makeRequest {
        api.auth(phoneNumber)
    }

    suspend fun getCities() = makeRequest {
        api.getCities()
    }

    suspend fun clientRegistration(
        userRequest: UserRequest
    ) = makeRequest {
        api.clientRegistration(userRequest)
    }

    suspend fun sellerRegistration(
        userRequest: UserRequest
    ) = makeRequest {
        api.sellerRegistration(userRequest)
    }

    suspend fun postCertificates(
        certificates: List<MultipartBody.Part>
    ) = makeRequest {
        api.postCertificates(certificates)
    }

    suspend fun getPrivacyPolicy() = makeRequest {
        api.getPrivacyPolicy()
    }

    suspend fun sendDeviceTokenId(deviceTokenId: String) = withContext(Dispatchers.IO) {
        api.sendDeviceTokenId(deviceTokenId)
    }
}