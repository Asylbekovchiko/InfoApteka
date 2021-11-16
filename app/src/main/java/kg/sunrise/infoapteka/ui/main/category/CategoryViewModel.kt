package kg.sunrise.infoapteka.ui.main.category

import kg.sunrise.infoapteka.base.viewModel.BaseViewModel
import kg.sunrise.infoapteka.networking.models.response.CategoryItemResponse
import kg.sunrise.infoapteka.networking.repositories.DrugRepository
import kg.sunrise.infoapteka.utils.network.State

class CategoryViewModel(
    private val drugRepository: DrugRepository
) : BaseViewModel() {

    var currentCategories = ArrayList<CategoryItemResponse>()

    fun getCategories() = getViewModelScope {
        val response = drugRepository.getCategories()

        if (!response.hasBody()) return@getViewModelScope

        if (response!!.isSuccess()) {
            _state.value = State.SuccessState.SuccessObjectState(response.body())
        }
    }
}