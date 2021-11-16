package kg.sunrise.infoapteka.ui.shared.adressesPaging

import androidx.paging.PagingState
import kg.sunrise.infoapteka.base.paging.BasePagingSource
import kg.sunrise.infoapteka.networking.models.response.BranchAdressesPaging
import kg.sunrise.infoapteka.networking.models.response.BranchAdressesResponse
import retrofit2.Response

class AdressesPagingSource(getBranchesCallback: suspend (Int) -> Response<BranchAdressesPaging>?)
    : BasePagingSource<BranchAdressesPaging , BranchAdressesResponse>(getBranchesCallback) {
    override fun getNextKey(responseBody: BranchAdressesPaging): Int? {
        return responseBody.next
    }

    override fun getResponseItem(responseBody: BranchAdressesPaging): ArrayList<BranchAdressesResponse> {
        return responseBody.results
    }
}