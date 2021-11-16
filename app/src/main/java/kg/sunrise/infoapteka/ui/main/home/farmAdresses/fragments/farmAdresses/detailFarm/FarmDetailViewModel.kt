package kg.sunrise.infoapteka.ui.main.home.farmAdresses.fragments.farmAdresses.detailFarm

import kg.sunrise.infoapteka.base.viewModel.BaseViewModel
import kg.sunrise.infoapteka.networking.repositories.FarmRepository
import kg.sunrise.infoapteka.utils.extensions.updateValue
import kg.sunrise.infoapteka.utils.network.State

class FarmDetailViewModel(private val farmRepository: FarmRepository) : BaseViewModel(){

    fun getDetailInfo(id : Int) = getViewModelScope {
        val response = farmRepository.getDetailInfoFarm(id)
        if(!response.hasBody())
            return@getViewModelScope
        if(response!!.isSuccess())
            _state updateValue State.SuccessState.SuccessObjectState(response!!.body())
    }
}