package kg.sunrise.infoapteka.networking.repositories

import kg.sunrise.infoapteka.base.repository.BaseRepository
import kg.sunrise.infoapteka.networking.Result
import kg.sunrise.infoapteka.networking.api.DrugAPI
import kg.sunrise.infoapteka.networking.api.ProfileApi
import kg.sunrise.infoapteka.networking.models.request.OrderIdRequest
import kg.sunrise.infoapteka.networking.models.request.OrderRequest
import kg.sunrise.infoapteka.networking.models.request.ProductRequest
import kg.sunrise.infoapteka.networking.models.response.OrderIdResponse
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class DrugRepository(
    private val drugAPI: DrugAPI,
    private val profileApi: ProfileApi
): BaseRepository() {

    suspend fun getCategories() = makeRequest {
        drugAPI.getCategories()
    }

    suspend fun postProduct(
        productRequest: ProductRequest
    ) = makeRequest {
        drugAPI.postDrug(productRequest)
    }

    suspend fun getSearchHints(search: String) = makeRequest {
        drugAPI.getSearchHints(search)
    }

    suspend fun addToFavorites(productId: Int) = makeRequest {
        drugAPI.addToFavorites(productId)
    }

    suspend fun removeFromFavorites(productId: Int) = makeRequest {
        drugAPI.removeFromFavorites(productId)
    }

    suspend fun addToBasket(productId: Int) = makeRequest {
        drugAPI.addToBasket(productId)
    }

    suspend fun removeFromBasket(productId: Int) = makeRequest {
        drugAPI.removeFromBasket(productId)
    }

    suspend fun uploadImages(
        drugId: Int,
        images: ArrayList<MultipartBody.Part>
    ) = makeRequest {
        drugAPI.uploadImages(drugId, images)
    }

    suspend fun getDrug(drugId: Int) = makeRequest {
        drugAPI.getDrug(drugId)
    }

    suspend fun putDrug(
        drugId: Int, productRequest: ProductRequest
    ) = makeRequest {
        drugAPI.putDrug(drugId, productRequest)
    }

    suspend fun setOrdering(data: OrderRequest) = makeRequest {
        drugAPI.setOrdering(data)
    }

    suspend fun getUser() = makeRequest {
        profileApi.getSeller()
    }

    suspend fun deleteDrug(id: Int) = makeRequest {
        drugAPI.deleteDrug(id)
    }

    suspend fun payWithElsom(orderID: Int): Result<OrderIdResponse> {
        val orderRequest = OrderIdRequest(orderID)

//        try {
            val response = drugAPI.payWithElsom(orderRequest)
            if (response.isSuccessful && response.body() != null) {
                return Result.Data(response.body()) as Result<OrderIdResponse> }
//
//            }
//        } catch (e: Throwable) {

//        }

        return Result.Error("server error")
    }
}