package kg.sunrise.infoapteka.ui.splashScreen.preface

import kg.sunrise.infoapteka.base.viewModel.BaseViewModel
import kg.sunrise.infoapteka.networking.repositories.InfoRepository
import kg.sunrise.infoapteka.utils.network.State

class PrefaceViewModel(
        private val infoRepository: InfoRepository
) : BaseViewModel() {

    var isPrefaceEnd = false
    var isVersionValid = false

    fun getAppVersion() = getViewModelScope {
        val response = infoRepository.getAppVersion()

        if(!response.hasBody()) return@getViewModelScope

        if (response!!.isSuccess()){
            _state.value = State.SuccessState.SuccessObjectState(response.body())
        }
    }

}