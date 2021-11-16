package kg.sunrise.infoapteka.modules

import kg.sunrise.infoapteka.App.Companion.context
import kg.sunrise.infoapteka.networking.api.*
import org.koin.dsl.module
import retrofit2.Retrofit

private val retrofit : Retrofit = createNetworkClient(context)
private val PROFILE_API : ProfileApi = retrofit.create(ProfileApi::class.java)
private val authApi: AuthApi = retrofit.create(AuthApi::class.java)
private val INFO_API : InfoAPI = retrofit.create(InfoAPI::class.java)
private val DRUG_API : DrugAPI = retrofit.create(DrugAPI::class.java)
private val NOTIFICATION_API : NotificationApi = retrofit.create(NotificationApi::class.java)

val networkModule = module {
    single { PROFILE_API }
    single { authApi }
    single { DRUG_API }
    single { INFO_API }
    single { NOTIFICATION_API }
}