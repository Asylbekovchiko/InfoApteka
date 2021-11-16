package kg.sunrise.infoapteka.ui.shared.productPaging

import kg.sunrise.infoapteka.base.paging.BasePagingSource
import kg.sunrise.infoapteka.networking.models.response.ProductResponse
import kg.sunrise.infoapteka.networking.models.response.ProductsPagingResponse
import retrofit2.Response

class ProductPagingSource(
    getProductsCallback: suspend (Int) -> Response<ProductsPagingResponse>?
) : BasePagingSource<ProductsPagingResponse, ProductResponse>(getProductsCallback) {

    override fun getNextKey(responseBody: ProductsPagingResponse): Int? {
        return responseBody.next
    }

    override fun getResponseItem(responseBody: ProductsPagingResponse): ArrayList<ProductResponse> {
        return responseBody.products
    }
}