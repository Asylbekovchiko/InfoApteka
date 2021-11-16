package kg.sunrise.infoapteka.utils.network

sealed class State {
    class LoadingState(val isLoading: Boolean) : State()
    class ErrorState(val message: String, val errorCode: Int) : State()
    sealed class SuccessState : State() {
        class SuccessListState<T>(val data: ArrayList<T>) : SuccessState()
        class SuccessObjectState<T>(val data: T) : SuccessState()
        object NoItemState : SuccessState()
    }
}