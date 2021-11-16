package kg.sunrise.infoapteka.base.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kg.sunrise.infoapteka.utils.constants.DEFAULT_INTEGER
import kg.sunrise.infoapteka.utils.constants.DEFAULT_NO_INTERNER_INTEGER
import kg.sunrise.infoapteka.utils.network.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.InetAddress
import java.net.UnknownHostException

abstract class BaseViewModel : ViewModel() {

    protected val _state = MutableLiveData<State>()
    val state : LiveData<State> = _state

    var hasInternet = true

    protected fun getViewModelScope(function: suspend () -> Unit) = viewModelScope.launch {
        if (!hasConnection()) return@launch
        _state.value = State.LoadingState(true)
        function()
        _state.value = State.LoadingState(false)
    }

    protected suspend fun hasConnection() : Boolean {
        return try {
            withContext(Dispatchers.Default) { InetAddress.getByName("google.com") }
            true
        } catch (e: UnknownHostException) {
            _state.value = State.ErrorState("", DEFAULT_NO_INTERNER_INTEGER)
            false
        }
    }

    fun<T> Response<T>?.hasBody() : Boolean {
        return if (this != null) {
            true
        } else {
            _state.value = State.ErrorState("", DEFAULT_INTEGER)
            false
        }
    }

    fun<T> Response<T>.isSuccess() : Boolean {
        return if (isSuccessful) {
            true
        } else {
            _state.value = State.ErrorState(errorBody()!!.string(), code())
            false
        }
    }

    fun checkInternetConnection() {
        viewModelScope.launch {
            val hasInternet = hasConnection()
            if (hasInternet)
                _state.value = State.SuccessState.NoItemState
        }
    }
}