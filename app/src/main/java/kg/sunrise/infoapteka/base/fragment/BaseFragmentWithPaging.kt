package kg.sunrise.infoapteka.base.fragment

import androidx.viewbinding.ViewBinding
import kg.sunrise.infoapteka.base.viewModel.BaseViewModelPaging
import kotlinx.coroutines.Job

abstract class BaseFragmentWithPaging<Item: Any, Binding : ViewBinding, out VM : BaseViewModelPaging<Item>>
    : BaseFragmentWithVM<Binding, VM>() {

    protected var requestJob: Job? = null

    override fun makeRequests() {
        viewModel.clearPaging()
    }
}