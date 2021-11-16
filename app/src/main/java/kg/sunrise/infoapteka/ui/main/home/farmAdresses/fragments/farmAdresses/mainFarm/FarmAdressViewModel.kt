package kg.sunrise.infoapteka.ui.main.home.farmAdresses.fragments.farmAdresses.mainFarm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.google.android.gms.maps.model.LatLng
import kg.sunrise.infoapteka.base.viewModel.BaseViewModelPaging
import kg.sunrise.infoapteka.networking.models.response.BranchAdressesPaging
import kg.sunrise.infoapteka.networking.models.response.BranchAdressesResponse
import kg.sunrise.infoapteka.networking.repositories.FarmRepository
import kg.sunrise.infoapteka.utils.extensions.updateValue
import kg.sunrise.infoapteka.utils.network.State
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class FarmAdressViewModel(private val repo: FarmRepository) :
    BaseViewModelPaging<BranchAdressesResponse>() {
    override var cashedData: Flow<PagingData<BranchAdressesResponse>>? = null
    var getBranchLive = MutableLiveData<BranchAdressesPaging>()
    val branchData = arrayListOf<BranchAdressesResponse>()
    fun getAdressesPaging(
        latitude: Double?, langtitude: Double?
    ): Flow<PagingData<BranchAdressesResponse>> {
        val lastResult = cashedData

        if (lastResult != null)
            return lastResult

        val result = repo.getAdressByPage { page ->
            getAdresses(page , latitude , langtitude)
        }.cachedIn(viewModelScope)

        cashedData = result
        return result
    }

    private suspend fun getAdresses(
        page: Int,
        latitude: Double?, langtitude: Double?
    ): Response<BranchAdressesPaging>? {
        return getResponsePaging {
            val response = repo.getAdreses(page , latitude , langtitude)
            if (!response.hasBody())
                return@getResponsePaging null
            if (response!!.isSuccess()) {
                branchData.addAll(response.body()!!.results)
                getBranchLive updateValue response.body()!!
                _state updateValue State.SuccessState.NoItemState

            }
            return@getResponsePaging response
        }
    }
}
