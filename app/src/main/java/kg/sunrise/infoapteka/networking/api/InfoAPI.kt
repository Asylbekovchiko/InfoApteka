package kg.sunrise.infoapteka.networking.api

import kg.sunrise.infoapteka.networking.models.response.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface InfoAPI {

    @GET("info/about/")
    suspend fun getAboutCompany(): Response<AboutCompanyInfo>

    @GET("info/delivery-condition/")
    suspend fun getDeliveryCondition(): Response<InfoResponse>

    @GET("info/privacy-policy/")
    suspend fun getPrivacyPolicy(): Response<InfoResponse>

    @GET("info/handbook/")
    suspend fun getHelpInfo(): Response<ArrayList<HelpInfoResponse>>

    @GET("info/banner/")
    suspend fun getBanners(): Response<ArrayList<BannerResponse>>

    @GET("setting/version/")
    suspend fun getAppVersion(): Response<AppVersion>


    @GET("info/branch")
    suspend fun getAdresses(@Query("page") page : Int ,@Query("lat") latitude : Double?, @Query("lng") langtidude : Double?) : Response<BranchAdressesPaging>

    @GET("info/branch/{id}")
    suspend fun getDetailInfoFarm(@Path("id") id : Int) : Response<BranchResponseDetail>


}