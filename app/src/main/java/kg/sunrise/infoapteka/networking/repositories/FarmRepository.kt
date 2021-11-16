package kg.sunrise.infoapteka.networking.repositories

import androidx.paging.PagingData
import kg.sunrise.infoapteka.base.repository.BaseRepositoryPaging
import kg.sunrise.infoapteka.networking.api.InfoAPI
import kg.sunrise.infoapteka.networking.models.response.BranchAdressesPaging
import kg.sunrise.infoapteka.networking.models.response.BranchAdressesResponse
import kg.sunrise.infoapteka.ui.shared.adressesPaging.AdressesPagingSource
import kg.sunrise.infoapteka.utils.constants.DEFAULT_PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class FarmRepository(private val api : InfoAPI) : BaseRepositoryPaging() {

    fun getAdressByPage(
       getAdressResponse: suspend (Int) -> Response<BranchAdressesPaging>?
    ) : Flow<PagingData<BranchAdressesResponse>> {
        return getDataByPage(
            DEFAULT_PAGE_SIZE,
            AdressesPagingSource { page ->
                getAdressResponse(page)
            }
        )
    }

    suspend fun getAdreses(page: Int , latitude : Double? , longtitude :Double?): Response<BranchAdressesPaging>? = makeRequest {
            api.getAdresses(page , latitude , longtitude)
        }

    suspend fun getDetailInfoFarm(id : Int)= makeRequest {
        api.getDetailInfoFarm(id)
    }
    }
