package kg.sunrise.infoapteka.networking.repositories

import androidx.paging.PagingData
import kg.sunrise.infoapteka.base.repository.BaseRepositoryPaging
import kg.sunrise.infoapteka.enums.OrderHistoryType
import kg.sunrise.infoapteka.networking.api.DrugAPI
import kg.sunrise.infoapteka.networking.models.response.*
import kg.sunrise.infoapteka.ui.main.menu.ordersHistory.adapter.OrderHistoryDTO
import kg.sunrise.infoapteka.ui.main.menu.ordersHistory.adapter.ProductHistoryDTO
import kg.sunrise.infoapteka.ui.shared.productPaging.ProductPagingSource
import kg.sunrise.infoapteka.utils.constants.DEFAULT_PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class ProductRepository(
    private val drugAPI: DrugAPI
) : BaseRepositoryPaging() {

    fun getProductsByPage(
        getProductsCallback: suspend (Int) -> Response<ProductsPagingResponse>?
    ) : Flow<PagingData<ProductResponse>> {
        return getDataByPage(
            DEFAULT_PAGE_SIZE,
            ProductPagingSource() { page ->
                getProductsCallback(page)
            }
        )
    }

    suspend fun getProducts(page: Int, filter: String?, categoryId: Int?, ordering: String?, search: String?, ownerId: Int?): Response<ProductsPagingResponse>? {
        return makeRequest {
            drugAPI.getProductsPagingBy(page, filter, categoryId, ordering, search, ownerId)
        }
    }

    suspend fun getFavoriteProducts(page: Int): Response<ProductsPagingResponse>? {
        return makeRequest {
            drugAPI.getFavoriteProductsByPage(page)
        }
    }

    suspend fun getProductDetail(productId: Int) = makeRequest {
         drugAPI.getProductDetail(productId)
    }

    suspend fun getSellerInfoBasket(id: Int): Response<SellerInfoResponse>? {
        return makeRequest {
            drugAPI.getSellerInfoInBasket(id)
        }
    }

    fun getMyProductsByPage(
        getProductsCallback: suspend (Int) -> Response<DrugPagingResponse>?
    ): Flow<PagingData<MyDrug>> {
        return getDataByPage(
            DEFAULT_PAGE_SIZE,
            MyProductsSource() { page ->
                getProductsCallback(page)
            }
        )
    }

    suspend fun getMyProducts(page: Int) = makeRequest {
        drugAPI.getMyProducts(page)
    }

    suspend fun getMyOrderHistory() = makeRequest {
        drugAPI.getOrderHistory()
    }

    suspend fun deleteDrug(id: Int) = makeRequest {
        drugAPI.deleteDrug(id)
    }
}