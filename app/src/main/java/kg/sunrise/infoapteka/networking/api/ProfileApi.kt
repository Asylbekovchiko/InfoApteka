package kg.sunrise.infoapteka.networking.api


import kg.sunrise.infoapteka.networking.models.request.CustomerRequest
import kg.sunrise.infoapteka.networking.models.response.*
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ProfileApi {

    @GET("account/avatar/")
    suspend fun getAvatar(): Response<AvatarResponse>

    @Multipart
    @PUT("account/avatar/")
    suspend fun setAvatar(@Part avatar: MultipartBody.Part): Response<AvatarResponse>

    @DELETE("account/avatar/")
    suspend fun deleteAvatar(): Response<*>

    @GET("setting/cities/")
    suspend fun getCities(): Response<ArrayList<City>>

    @Multipart
    @POST("account/certificate/")
    suspend fun setCertificates(@Part image: ArrayList<MultipartBody.Part>): Response<ArrayList<SertficateResponse>>

    @PUT("account/seller-user-update/")
    suspend fun setUpdateProfileSeller(
        @Body profileUpdate: SellerProfileUpdate
    ): Response<*>

    @GET("account/data/")
    suspend fun getCustomer(): Response<CustomerResponse>

    @GET("account/data/")
    suspend fun getSeller() : Response<SellerProfileResponse>

    @PUT("account/")
    suspend fun updateCustomer(@Body data: CustomerRequest): Response<CustomerUpdateResponse>
}