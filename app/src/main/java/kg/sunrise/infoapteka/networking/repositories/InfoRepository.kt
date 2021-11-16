package kg.sunrise.infoapteka.networking.repositories

import androidx.paging.PagingData
import kg.sunrise.infoapteka.base.repository.BaseRepository
import kg.sunrise.infoapteka.networking.api.InfoAPI


class InfoRepository(
    private val infoAPI: InfoAPI
): BaseRepository() {

    suspend fun getAboutCompanyInfo() = makeRequest {
        infoAPI.getAboutCompany()
    }

    suspend fun getDeliveryCondition() = makeRequest {
        infoAPI.getDeliveryCondition()
    }

    suspend fun getPrivacyPolicy() = makeRequest {
        infoAPI.getPrivacyPolicy()
    }

    suspend fun getHelpInfo() = makeRequest {
        infoAPI.getHelpInfo()
    }

    suspend fun getBanners() = makeRequest {
        infoAPI.getBanners()
    }

    suspend fun getAppVersion() = makeRequest {
        infoAPI.getAppVersion()
    }
}