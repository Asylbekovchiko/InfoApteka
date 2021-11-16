package kg.sunrise.infoapteka.ui.main.basket.seller

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.viewModel.BaseViewModelPaging
import kg.sunrise.infoapteka.enums.SortType
import kg.sunrise.infoapteka.networking.models.response.ProductResponse
import kg.sunrise.infoapteka.networking.models.response.ProductsPagingResponse
import kg.sunrise.infoapteka.networking.repositories.DrugRepository
import kg.sunrise.infoapteka.networking.repositories.ProductRepository
import kg.sunrise.infoapteka.ui.shared.searchFragment.searchResult.RadioBtnItem
import kg.sunrise.infoapteka.utils.network.State
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class SellerViewModel(
    private val drugRepository: DrugRepository,
    private val productRepository: ProductRepository,

    ) : BaseViewModelPaging<ProductResponse>() {

    override var cashedData: Flow<PagingData<ProductResponse>>? = null

    fun getSellerInfo(id: Int) = getViewModelScope {
        val response = productRepository.getSellerInfoBasket(id)

        if(!response.hasBody()) return@getViewModelScope

        if (response!!.isSuccess()){
            _state.value = State.SuccessState.SuccessObjectState(response.body())
        }
    }


    fun getProductByPaging(
        filter: String?,
        categoryID: Int?,
        ordering: String?,
        search: String?,
        ownerId: Int?
    ): Flow<PagingData<ProductResponse>> {
        val lastResult = cashedData

        if (lastResult != null)
            return lastResult

        val result = productRepository.getProductsByPage { page ->
            getProducts(page, filter, categoryID, ordering, search, ownerId)
        }.cachedIn(viewModelScope)

        cashedData = result

        return result
    }

    private suspend fun getProducts(
        page: Int,
        filter: String? = null,
        categoryID: Int?,
        ordering: String?,
        search: String?,
        ownerId: Int?
    ): Response<ProductsPagingResponse>? {
        return getResponsePaging {
            val response = productRepository.getProducts(page, filter, categoryID, ordering, search, ownerId)

            if (!response.hasBody()) return@getResponsePaging null

            if (response!!.isSuccess()) {
                _state.value = State.SuccessState.NoItemState
            }
            return@getResponsePaging response
        }
    }

    var sort = getSortItems()
    var sortType = SortType.DEFAULT
    var productName: String? = null
    var productCategoryID: Int? = null
    var spanCount = 2

    private fun getSortItems(): ArrayList<RadioBtnItem> {
        return arrayListOf(
            RadioBtnItem(R.string.sort_price_asc, SortType.PRICE_ASC, false),
            RadioBtnItem(R.string.sort_price_desc, SortType.PRICE_DESC, false),
            RadioBtnItem(R.string.sort_alphabet_asc, SortType.ALPHABET_ASC, false),
            RadioBtnItem(R.string.sort_alphabet_desc, SortType.ALPHABET_DESC, false),
            RadioBtnItem(R.string.sort_new_asc, SortType.NEW_ASC, false),
            RadioBtnItem(R.string.sort_new_desc, SortType.NEW_DESC, false),
            RadioBtnItem(R.string.sort_bestseller, SortType.BESTSELLER, false),
        )
    }

    fun addAndDeleteFromFavorites(isFavorite: Boolean, productId: Int) = getViewModelScope {
        if (isFavorite) {
            drugRepository.addToFavorites(productId)
        } else {
            drugRepository.removeFromFavorites(productId)
        }
    }

    fun addAndDeleteFromBasket(added: Boolean, productId: Int) = getViewModelScope {
        if (added) {
            drugRepository.addToBasket(productId)
        } else {
            drugRepository.removeFromBasket(productId)
        }
    }
}