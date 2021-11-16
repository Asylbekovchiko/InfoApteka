package kg.sunrise.infoapteka.ui.main.menu.myProducts

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import kg.sunrise.infoapteka.base.viewModel.BaseViewModelPaging
import kg.sunrise.infoapteka.networking.models.response.DrugPagingResponse
import kg.sunrise.infoapteka.networking.models.response.mapToMyProductDTO
import kg.sunrise.infoapteka.networking.repositories.ProductRepository
import kg.sunrise.infoapteka.ui.main.menu.myProducts.adapter.MyProductDTO
import kg.sunrise.infoapteka.utils.constants.DRUG_DELETED_SUCCESSFULLY
import kg.sunrise.infoapteka.utils.extensions.updateSuccessObjectValue
import kg.sunrise.infoapteka.utils.network.State
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Response

class MyProductsViewModel(
    private val productRepository: ProductRepository,
) : BaseViewModelPaging<MyProductDTO>() {

    override var cashedData: Flow<PagingData<MyProductDTO>>? = null

    fun getMyProductPaging(): Flow<PagingData<MyProductDTO>> {
        val lastResult = cashedData

        if (lastResult != null)
            return lastResult

        val result = productRepository.getMyProductsByPage { page ->
            getMyProducts(page)
        }.map {
            it.map {
                it.mapToMyProductDTO()
            }
        }.cachedIn(viewModelScope)

        cashedData = result

        return result
    }

    private suspend fun getMyProducts(
        page: Int
    ): Response<DrugPagingResponse>? {
        return getResponsePaging {
            val response = productRepository.getMyProducts(page)

            if (!response.hasBody()) return@getResponsePaging null

            if (response!!.isSuccess()) {
                _state.value = State.SuccessState.NoItemState
            }

            return@getResponsePaging response
        }
    }

    fun deleteDrug(drugId: Int) = getViewModelScope {
        val response = productRepository.deleteDrug(drugId)

        if (!response.hasBody()) return@getViewModelScope

        if (response!!.isSuccess()) {
            _state updateSuccessObjectValue DRUG_DELETED_SUCCESSFULLY
        }
    }
}