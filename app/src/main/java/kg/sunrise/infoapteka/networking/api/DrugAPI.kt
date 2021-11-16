package kg.sunrise.infoapteka.networking.api

import kg.sunrise.infoapteka.networking.Result
import kg.sunrise.infoapteka.networking.models.request.OrderIdRequest
import kg.sunrise.infoapteka.networking.models.request.OrderRequest
import kg.sunrise.infoapteka.networking.models.request.ProductRequest
import kg.sunrise.infoapteka.networking.models.response.*
import kg.sunrise.infoapteka.ui.main.menu.ordersHistory.adapter.OrderHistoryDTO
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface DrugAPI {

    @GET("drug/categories/")
    suspend fun getCategories(): Response<ArrayList<CategoryItemResponse>>

    @GET("drug/list/")
    suspend fun getProductsPagingBy(
        @Query("page") page: Int,
        @Query("filter") filter: String?,
        @Query("category_id") categoryId: Int?,
        @Query("ordering") ordering: String?,
        @Query("search") search: String?,
        @Query("owner_id") ownerId: Int?
    ): Response<ProductsPagingResponse>

    @GET("account/favorite/")
    suspend fun getFavoriteProductsByPage(
        @Query("page") page: Int
    ): Response<ProductsPagingResponse>

    @GET("drug/search_hint/")
    suspend fun getSearchHints(
        @Query("search") search: String
    ): Response<ArrayList<String>>

    @POST("account/favorite/{id}/add/")
    suspend fun addToFavorites(
        @Path("id") productId: Int
    ): Response<*>

    @DELETE("account/favorite/{id}/remove/")
    suspend fun removeFromFavorites(
        @Path("id") productId: Int
    ): Response<*>

    @POST("cart/{id}/add/")
    suspend fun addToBasket(
        @Path("id") productId: Int
    ): Response<*>

    @DELETE("cart/{id}/delete/")
    suspend fun removeFromBasket(
        @Path("id") productId: Int
    ): Response<*>

    @POST("cart/{id}/remove/")
    suspend fun removeItemFromBasket(@Path("id") id : Int) : Response<*>

    @GET("cart/")
    suspend fun getCartItems() : Response<BasketResponse>

    @GET("drug/{id}/")
    suspend fun getProductDetail(
        @Path("id") productId: Int
    ): Response<ProductDetailResponse>

    @GET("drug/seller/{id}")
    suspend fun getSellerInfoInBasket(
        @Path("id") ownerId: Int
    ): Response<SellerInfoResponse>

    @POST("account/drug/")
    suspend fun postDrug(
        @Body productRequest: ProductRequest
    ): Response<EditProductResponse>

    @Multipart
    @POST("account/image/{id}/")
    suspend fun uploadImages(
        @Path("id") id: Int,
        @Part image: ArrayList<MultipartBody.Part>
    ): Response<ArrayList<PhotoResponse>>

    @GET("account/drug/{id}")
    suspend fun getDrug(
        @Path("id") drugId: Int
    ): Response<EditDrugResponse>

    @GET("account/drug")
    suspend fun getMyProducts(
        @Query("page") page: Int
    ): Response<DrugPagingResponse>

    @PUT("account/drug/{id}/")
    suspend fun putDrug(
        @Path("id") id: Int,
        @Body productRequest: ProductRequest
    ): Response<EditProductResponse>

    @POST("order/")
    suspend fun setOrdering(@Body data: OrderRequest): Response<OrderingResponse>

    @DELETE("account/drug/{id}/")
    suspend fun deleteDrug(
        @Path("id") drugId: Int
    ): Response<ResponseBody>

    @GET("order/")
    suspend fun getOrderHistory(): Response<ArrayList<OrderHistoryDTO>>

    @POST("order/payment/elsom_payment/")
    suspend fun payWithElsom(
        @Body orderRequest: OrderIdRequest
    ): Response<OrderIdResponse>
}