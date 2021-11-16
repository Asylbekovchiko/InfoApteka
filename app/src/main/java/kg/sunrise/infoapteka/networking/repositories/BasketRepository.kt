package kg.sunrise.infoapteka.networking.repositories

import kg.sunrise.infoapteka.base.repository.BaseRepository
import kg.sunrise.infoapteka.networking.api.DrugAPI

class BasketRepository(private val api : DrugAPI) : BaseRepository() {
    suspend fun getCartItems() = makeRequest{
        api.getCartItems()
        }

    suspend fun removeItemFromBasket(id : Int) = makeRequest {
        api.removeItemFromBasket(id)
    }

    suspend fun addToFavourites(id : Int) = makeRequest {
        api.addToFavorites(id)
    }

    suspend fun addItemToBasket(id : Int) = makeRequest {
        api.addToBasket(id)
    }

    suspend fun removeFromFavourites(id : Int) = makeRequest {
        api.removeFromFavorites(id)
    }
    suspend fun removeFromBasket(id : Int) = makeRequest {
        api.removeFromBasket(id)
    }
    }