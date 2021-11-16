package kg.sunrise.infoapteka.ui.shared

import kg.sunrise.infoapteka.base.viewModel.BaseViewModel
import kg.sunrise.infoapteka.networking.models.response.HelpInfoResponse
import kg.sunrise.infoapteka.networking.repositories.InfoRepository
import kg.sunrise.infoapteka.utils.network.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import java.net.URL

class InfoViewModel(
    private val infoRepository: InfoRepository
): BaseViewModel() {

    fun getCompanyInfo() = getViewModelScope {
        val response = infoRepository.getAboutCompanyInfo()

        if (!response.hasBody()) return@getViewModelScope

        if (response!!.isSuccess()) {
            _state.value = State.SuccessState.SuccessObjectState(response.body())
        }
    }

    fun getDeliveryCondition() = getViewModelScope {
        val response = infoRepository.getDeliveryCondition()

        if (!response.hasBody()) return@getViewModelScope

        if (response!!.isSuccess()) {
            _state.value = State.SuccessState.SuccessObjectState(response.body())
        }
    }

    fun getHelpInfo() = getViewModelScope {
        val response = infoRepository.getHelpInfo()

        if (!response.hasBody()) return@getViewModelScope

        if (response!!.isSuccess()) {
            _state.value = State.SuccessState.SuccessObjectState(response.body())
        }
    }

    suspend fun getFileFromPdfURL(pdfURL: String): File {
        return withContext(Dispatchers.IO) {
            val stream = URL(pdfURL).openStream()
            val file = File.createTempFile("tempFile", ".pdf")
            getFileFromStream(stream, file)
            file.deleteOnExit()

            return@withContext file
        }
    }

    private fun getFileFromStream(inputStream: InputStream, output: File) {
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
}