package kg.sunrise.infoapteka.ui.main.home

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kg.sunrise.infoapteka.base.viewModel.BaseViewModelPaging
import kg.sunrise.infoapteka.networking.models.response.BannerResponse
import kg.sunrise.infoapteka.networking.models.response.ProductDetailResponse
import kg.sunrise.infoapteka.networking.models.response.ProductResponse
import kg.sunrise.infoapteka.networking.models.response.ProductsPagingResponse
import kg.sunrise.infoapteka.networking.repositories.DrugRepository
import kg.sunrise.infoapteka.networking.repositories.InfoRepository
import kg.sunrise.infoapteka.networking.repositories.ProductRepository
import kg.sunrise.infoapteka.utils.network.State
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class HomeViewModel(
    private val productRepository: ProductRepository,
    private val drugRepository: DrugRepository,
    private val infoRepository: InfoRepository
) : BaseViewModelPaging<ProductResponse>() {

    override var cashedData: Flow<PagingData<ProductResponse>>? = null

    fun getProductPaging(
        filter: String? = null,
        categoryId: Int? = null,
        ordering: String? = null,
        search: String? = null,
        ownerId: Int?
    )
            : Flow<PagingData<ProductResponse>> {
        val lastResult = cashedData

        if (lastResult != null)
            return lastResult

        val result = productRepository.getProductsByPage { page ->
            getProducts(page, filter, categoryId, ordering, search, ownerId)
        }.cachedIn(viewModelScope)

        cashedData = result

        return result
    }

    private suspend fun getProducts(
        page: Int,
        filter: String?,
        categoryId: Int?,
        ordering: String?,
        search: String?,
        ownerId: Int?
    ): Response<ProductsPagingResponse>? {
        return getResponsePaging {
            val response = productRepository.getProducts(page, filter, categoryId, ordering, search, ownerId)

            if (!response.hasBody()) return@getResponsePaging null

            if (response!!.isSuccess()) {
                _state.value = State.SuccessState.NoItemState
            }

            return@getResponsePaging response
        }
    }

    fun getProductDetail(productId: Int) = getViewModelScope {
        val response = productRepository.getProductDetail(productId)

        if (!response.hasBody()) return@getViewModelScope

        if (response!!.isSuccess()) {
            _state.value = State.SuccessState.SuccessObjectState(response.body())
        }
    }

    fun getMyOrderHistory() = getViewModelScope {
        val response = productRepository.getMyOrderHistory()

        if (!response.hasBody()) return@getViewModelScope

        if (response!!.isSuccess()) {
            _state.value = State.SuccessState.SuccessObjectState(response.body())
        }
    }

    fun getBanners() = getViewModelScope {
        val response = infoRepository.getBanners()

        if (!response.hasBody()) return@getViewModelScope

        if (response!!.isSuccess()) {
            _state.value = State.SuccessState.SuccessObjectState(response.body())
        }
    }

    fun addRemoveFavourite(isFavorite: Boolean, productId: Int) = getViewModelScope {
        if (isFavorite) {
            drugRepository.addToFavorites(productId)
        } else {
            drugRepository.removeFromFavorites(productId)
        }
    }

    fun addRemoveBasket(inBasket: Boolean, productId: Int) = getViewModelScope {
        if (inBasket) {
            drugRepository.addToBasket(productId)
        } else {
            drugRepository.removeFromBasket(productId)
        }
    }

    var productDetail: ProductDetailResponse? = null

    var cashedImages: ArrayList<BannerResponse>? = null
}