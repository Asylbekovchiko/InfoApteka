package kg.sunrise.infoapteka.base.fragment

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewbinding.ViewBinding
import kg.sunrise.infoapteka.base.viewModel.BaseViewModel
import kg.sunrise.infoapteka.utils.extensions.*
import kg.sunrise.infoapteka.utils.network.ErrorCode
import kg.sunrise.infoapteka.utils.network.State
import kg.sunrise.infoapteka.utils.network.parseErrorCode
import timber.log.Timber

abstract class BaseFragmentWithVM<Binding : ViewBinding, out VM : BaseViewModel> :
    BaseFragment<Binding>() {

    protected abstract val viewModel: VM
    protected abstract val progressBar: ConstraintLayout
    protected open var isToShowProgress = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initIncludeLayouts()
        initViewModel()
        initErrorStatus()
        bindRefreshBtn()
        makeRequests()
    }

    private fun bindRefreshBtn() {
        bindRefreshBtn {
            makeRequests()
        }
    }

    abstract fun makeRequests()

    protected open fun initIncludeLayouts() {}

    private fun initErrorStatus() {
        if (!viewModel.hasInternet) {
            displayErrorLayout(true, ErrorCode.NOINTERNET)
        }
    }

    private fun initViewModel() {
        viewModel.state.observe(viewLifecycleOwner, { state ->
            if (state == null) {

            } else {
                val state = state
                when (state) {
                    is State.LoadingState -> {
                        if (isToShowProgress) {
                            when {
                                state.isLoading -> progressBar.visible()
                                else -> progressBar.gone()
                            }
                        }
                    }

                    is State.ErrorState -> {
                        handleError(state)
                    }

                    is State.SuccessState -> {
                        displayErrorLayout(false)

                        when (state) {
                            is State.SuccessState.NoItemState -> {
                                successRequest()
                            }

                            is State.SuccessState.SuccessObjectState<*> -> {
                                findTypeOfObject(state.data)
                            }

                            is State.SuccessState.SuccessListState<*> -> {
                                findTypeOfListObject(state.data)
                            }
                            else -> {
                            }
                        }
                    }

                    else -> {
                    }
                }
            }
        })
    }

    abstract fun findTypeOfObject(data: Any?)

    abstract fun successRequest()

    protected open fun findTypeOfListObject(data: ArrayList<*>) {

    }


    private fun handleError(state: State.ErrorState) {
        Timber.e("err ${state.message}")
//        FirebaseCrashlytics.getInstance().also {
//            it.setCustomKey(state.errorCode.toString(), state.message)
//            it.log(state.message)
//            it.recordException(Throwable(state.errorCode.toString()))
//        }.sendUnsentReports()

        view?.let {
            parseErrorCode(
                it,
                state,
                displayErrorLayout = { isError, errorCode ->
                    displayErrorLayout(isError, errorCode)
                },
                handleCustomError = { message ->
                    handleCustomError(message)
                },
                showErrorAsDialog = { message ->
                    showErrorAsDialog(message)
                }
            )
        }
    }

    protected open fun handleCustomError(message: String) {
        showErrorAsDialog(message)
    }

    protected open fun showErrorAsDialog(message: String) {
        view?.let { requireContext().snackbar(it, message) }
    }

    private fun displayErrorLayout(isError: Boolean, code: ErrorCode? = null) {
        if (isError && code != null) {
            when (code) {
                ErrorCode.SERVER_UNRESPONSIBLE -> {
                    setServiceUnavailableVisibility(false)
                }
                ErrorCode.NOINTERNET -> {
                    viewModel.hasInternet = code != ErrorCode.NOINTERNET
                    setNoInternetLayoutVisibility(code != ErrorCode.NOINTERNET)
                }
            }
        } else {
            viewModel.hasInternet = true
            setNoInternetLayoutVisibility(true)
            setServiceUnavailableVisibility(true)
        }
    }
}