package kg.sunrise.infoapteka.networking.repositories

import kg.sunrise.infoapteka.base.paging.BasePagingSource
import kg.sunrise.infoapteka.networking.models.response.MyDrug
import kg.sunrise.infoapteka.networking.models.response.DrugPagingResponse
import retrofit2.Response

class MyProductsSource(
    getProductsCallback: suspend (Int) -> Response<DrugPagingResponse>?
) : BasePagingSource<DrugPagingResponse, MyDrug>(
    getProductsCallback
) {

    override fun getNextKey(responseBody: DrugPagingResponse): Int? {
        return responseBody.next
    }

    override fun getResponseItem(responseBody: DrugPagingResponse): ArrayList<MyDrug> {
        return responseBody.results
    }
}
