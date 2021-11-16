package kg.sunrise.infoapteka.modules


import kg.sunrise.infoapteka.networking.repositories.*
import kg.sunrise.infoapteka.networking.repositories.BasketRepository
import kg.sunrise.infoapteka.networking.repositories.DrugRepository
import kg.sunrise.infoapteka.networking.repositories.InfoRepository
import kg.sunrise.infoapteka.networking.repositories.NotificationRepository
import kg.sunrise.infoapteka.networking.repositories.ProductRepository
import kg.sunrise.infoapteka.networking.repositories.AuthRepository
import kg.sunrise.infoapteka.networking.repositories.ProfileRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { AuthRepository(get()) }
    single { NotificationRepository(get()) }
    single { DrugRepository(get(), get()) }
    single { ProductRepository(get()) }
    single { ProfileRepository(get()) }
    single { FarmRepository(get())}
    single { InfoRepository(get()) }
    single {BasketRepository(get())}
}