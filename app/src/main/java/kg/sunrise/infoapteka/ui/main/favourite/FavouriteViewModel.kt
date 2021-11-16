package kg.sunrise.infoapteka.ui.main.favourite

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kg.sunrise.infoapteka.base.viewModel.BaseViewModelPaging
import kg.sunrise.infoapteka.networking.repositories.ProductRepository
import kg.sunrise.infoapteka.networking.models.response.ProductResponse
import kg.sunrise.infoapteka.networking.models.response.ProductsPagingResponse
import kg.sunrise.infoapteka.networking.repositories.DrugRepository
import kg.sunrise.infoapteka.utils.constants.SUCCESS_REQUEST
import kg.sunrise.infoapteka.utils.network.State
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class FavouriteViewModel(
    private val productRepository: ProductRepository,
    private val drugRepository: DrugRepository
) : BaseViewModelPaging<ProductResponse>() {

    override var cashedData: Flow<PagingData<ProductResponse>>? = null

    fun getFavoriteProductPaging(): Flow<PagingData<ProductResponse>> {
        val lastResult = cashedData

        if (lastResult != null)
            return lastResult

        val result = productRepository.getProductsByPage { page ->
            getFavoriteProducts(page)
        }.cachedIn(viewModelScope)

        cashedData = result

        return result
    }

    private suspend fun getFavoriteProducts(
        page: Int
    ): Response<ProductsPagingResponse>? {
        return getResponsePaging {
            val response = productRepository.getFavoriteProducts(page)

            if (!response.hasBody()) return@getResponsePaging null

            if (response!!.isSuccess()) {
                _state.value = State.SuccessState.NoItemState
            }

            return@getResponsePaging response
        }
    }



    fun addRemoveFavourite(isFavorite: Boolean, productId: Int) = getViewModelScope {
        var response: Response<*>? = null
        if (isFavorite) {
            response = drugRepository.addToFavorites(productId)
        } else {
            response = drugRepository.removeFromFavorites(productId)
        }

        if (response != null) {
            _state.value = State.SuccessState.SuccessObjectState(SUCCESS_REQUEST)
        }
    }

    fun addRemoveBasket(inBasket: Boolean, productId: Int) = getViewModelScope {
        if (inBasket) {
            drugRepository.addToBasket(productId)
        } else {
            drugRepository.removeFromBasket(productId)
        }
    }
}