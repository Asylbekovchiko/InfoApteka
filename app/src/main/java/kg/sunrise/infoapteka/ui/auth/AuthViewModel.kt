package kg.sunrise.infoapteka.ui.auth
import android.content.Context
import android.net.Uri
import androidx.lifecycle.viewModelScope
import kg.sunrise.infoapteka.base.viewModel.BaseViewModel
import kg.sunrise.infoapteka.networking.models.request.UserRequest
import kg.sunrise.infoapteka.networking.repositories.AuthRepository
import kg.sunrise.infoapteka.ui.auth.city.mapToCityRadioBtnItem
import kg.sunrise.infoapteka.utils.extensions.parseListUri
import kg.sunrise.infoapteka.utils.network.State
import kg.sunrise.infoapteka.utils.preference.getDeviceToken
import kg.sunrise.infoapteka.utils.preference.isAuthorized
import kg.sunrise.infoapteka.utils.preference.isDeviceTokenExist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.URL

class AuthViewModel(
    private val repo: AuthRepository
) : BaseViewModel() {

    var userRequest: UserRequest? = null

    fun auth(phoneNumber: String) = getViewModelScope {
        val response = repo.auth(phoneNumber)

        if (!response.hasBody()) return@getViewModelScope

        if (response!!.isSuccess() && response.body() != null) {
            _state.value =
                State.SuccessState.SuccessObjectState(response.body())
        } else if (response.code() == 400) {
            _state.value = State.SuccessState.NoItemState
        }
    }

    fun getCities() = getViewModelScope {
        val response = repo.getCities()

        if (!response.hasBody()) return@getViewModelScope

        if (response!!.isSuccess() && response.body() != null) {
            val cityBtnItems = ArrayList(response.body()!!.map {
                it.mapToCityRadioBtnItem()
            })
            _state.value =
                State.SuccessState.SuccessListState(cityBtnItems)
        }
    }

    fun clientRegistration() = getViewModelScope {
        if (userRequest == null) return@getViewModelScope
        val response = repo.clientRegistration(userRequest!!)

        if (!response.hasBody()) return@getViewModelScope

        if (response!!.isSuccess()) {
            _state.value =
                State.SuccessState.SuccessObjectState(response.body())
        }
    }

    fun sellerRegistration() = getViewModelScope {
        if (userRequest == null) return@getViewModelScope
        val response = repo.sellerRegistration(userRequest!!)

        if (!response.hasBody()) return@getViewModelScope

        if (response!!.isSuccess()) {
            _state.value =
                State.SuccessState.SuccessObjectState(response.body())
        }
    }

    fun postCertificates(
        certificates: ArrayList<Uri>
    ) = getViewModelScope {
        val response = repo.postCertificates(
            parseListUri(certificates)
        )
        if (!response.hasBody()) return@getViewModelScope

        if (response!!.isSuccess() && response.body() != null) {
            _state.value =
                State.SuccessState.SuccessObjectState(response.body()!!)
        }
    }

    fun getPrivacyPolicy() = getViewModelScope {
        val response = repo.getPrivacyPolicy()

        if (!response.hasBody()) return@getViewModelScope

        if (response!!.isSuccess()) {
            _state.value =
                State.SuccessState.SuccessObjectState(response.body())
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun getFileFromPdfURL(
        pdfURL: String
    ): File = withContext(Dispatchers.IO) {
        val stream = URL(pdfURL).openStream()
        val file = File.createTempFile("tempFile", ".pdf")
        getFileFromStream(stream, file)
        file.deleteOnExit()

        return@withContext file
    }

    private fun getFileFromStream(
        inputStream: InputStream, output: File
    ) {
        var outputStream: OutputStream? = null
        try {
            outputStream = FileOutputStream(output)
            var read = 0
            val bytes = ByteArray(1024)
            while (inputStream.read(bytes).also { read = it } != -1) {
                outputStream.write(bytes, 0, read)
            }
        } finally {
            try {
                inputStream.close()
            } finally {
                outputStream?.close()
            }
        }
    }

    fun sendDeviceTokenId(context: Context) {
        viewModelScope.launch {
            if (isAuthorized(context) && isDeviceTokenExist(context)) {
                val token = getDeviceToken(context)
                val response = repo.sendDeviceTokenId(token)
                print(response)
            }
        }
    }
}