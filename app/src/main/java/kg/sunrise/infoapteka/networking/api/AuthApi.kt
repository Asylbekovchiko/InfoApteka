package kg.sunrise.infoapteka.networking.api

import kg.sunrise.infoapteka.networking.models.request.UserRequest
import kg.sunrise.infoapteka.networking.models.response.*
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {

    @FormUrlEncoded
    @POST("account/check_user/")
    suspend fun checkUser(
        @Field("phone") phoneNumber: String
    ): Response<CheckUserResponse>

    @FormUrlEncoded
    @POST("account/auth/")
    suspend fun auth(
        @Field("phone") phoneNumber: String
    ): Response<AuthResponse>

    @GET("setting/cities/")
    suspend fun getCities(): Response<ArrayList<City>>

    @POST("account/simple-user-registration/")
    suspend fun clientRegistration(
        @Body userRequest: UserRequest
    ): Response<RegistrationResponse>

    @POST("account/seller-user-registration/")
    suspend fun sellerRegistration(
        @Body userRequest: UserRequest
    ): Response<RegistrationResponse>

    @Multipart
    @POST("account/certificate/")
    suspend fun postCertificates(
        @Part certificates: List<MultipartBody.Part>
    ): Response<ArrayList<SertficateResponse>>

    @GET("info/privacy-policy/")
    suspend fun getPrivacyPolicy(): Response<PrivacyPolicyResponse>

    @FormUrlEncoded
    @POST("account/device/")
    suspend fun sendDeviceTokenId(
        @Field("registration_id") deviceTokenId: String,
        @Field("type") osType: String = "android"
    ): Response<ResponseBody>
}