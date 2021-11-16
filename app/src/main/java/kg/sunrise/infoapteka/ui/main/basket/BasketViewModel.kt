package kg.sunrise.infoapteka.ui.main.basket


import kg.sunrise.infoapteka.base.viewModel.BaseViewModel
import kg.sunrise.infoapteka.networking.repositories.BasketRepository
import kg.sunrise.infoapteka.utils.extensions.updateValue
import kg.sunrise.infoapteka.utils.network.State

class BasketViewModel(
        private val basketRepository: BasketRepository
) : BaseViewModel() {
    fun getBasketItems() = getViewModelScope {
        val response = basketRepository.getCartItems()
        if (!response.hasBody())
            return@getViewModelScope
        if (response!!.isSuccess())
            _state updateValue State.SuccessState.SuccessObjectState(response.body())
    }

    fun addItemToBasket(id: Int) = getViewModelScope {
        val response = basketRepository.addItemToBasket(id)
        if (!response.hasBody())
            return@getViewModelScope
        if (response!!.isSuccess())
            _state updateValue State.SuccessState.NoItemState
    }

    fun addToFavourites(id: Int) = getViewModelScope {
        val response = basketRepository.addToFavourites(id)
        if (response!!.isSuccess())
            _state updateValue State.SuccessState.NoItemState
        if (!response.hasBody())
            return@getViewModelScope
    }

    fun removeItemFromBasket(id: Int) = getViewModelScope {
        val response = basketRepository.removeItemFromBasket(id)
        if (response!!.isSuccess())
            _state updateValue State.SuccessState.NoItemState
        if (response.hasBody())
            return@getViewModelScope
    }

    fun removeFromBasket(id :Int) = getViewModelScope {
        val response = basketRepository.removeFromBasket(id)
        if(!response.hasBody())
            return@getViewModelScope
        if (response!!.isSuccess())
            _state updateValue State.SuccessState.NoItemState
    }

    fun removeFromFavourites(id: Int) = getViewModelScope {
        val response = basketRepository.removeFromFavourites(id)
        if (!response.hasBody())
            return@getViewModelScope
        if (response!!.isSuccess())
            _state updateValue State.SuccessState.NoItemState
    }
}