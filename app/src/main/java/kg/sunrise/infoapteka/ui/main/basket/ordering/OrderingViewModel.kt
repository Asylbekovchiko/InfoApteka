package kg.sunrise.infoapteka.ui.main.basket.ordering

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.viewModel.BaseViewModel
import kg.sunrise.infoapteka.networking.Result
import kg.sunrise.infoapteka.networking.models.request.OrderRequest
import kg.sunrise.infoapteka.networking.models.request.PhonesItem
import kg.sunrise.infoapteka.networking.repositories.DrugRepository
import kg.sunrise.infoapteka.utils.constants.DEFAULT_INTEGER_NO_VALUE
import kg.sunrise.infoapteka.utils.network.State
import kotlinx.coroutines.launch
import timber.log.Timber

class OrderingViewModel(private val drugRepo: DrugRepository) : BaseViewModel() {

    private var _progressBarVisibility = MutableLiveData<Boolean>(false)
    val progressBarVisibility: LiveData<Boolean>
        get() = _progressBarVisibility

    val firstName = MutableLiveData<String>()
    val lastName = MutableLiveData<String>()
    val phones = MutableLiveData<ArrayList<PhonesItem>>()
    val map = MutableLiveData<String>()
    val deliveryAddress = MutableLiveData<String>()
    val comments = MutableLiveData<String>()
    val radioBtnItem = MutableLiveData<String>()
    val wasPhoneChanged = MutableLiveData(false)
    val cartId = MutableLiveData<Int>()

    var orderNumber: String? = null

    private val listLiveData = listOf(firstName, lastName, phones, map, deliveryAddress, comments, radioBtnItem, wasPhoneChanged, cartId)

    val isValid = MediatorLiveData<Boolean>().apply {
        listLiveData.forEach {
            addSource(it){
                value = checkRequiredFields()
            }
        }
    }

    private fun checkRequiredFields(): Boolean {
        return (!firstName.value.isNullOrBlank()
                && !phones.value.isNullOrEmpty()
                && !lastName.value.isNullOrBlank()
                && !map.value.isNullOrBlank()
                && !deliveryAddress.value.isNullOrBlank()
                && !radioBtnItem.value.isNullOrBlank()
                && checkPhones()!!)
    }

    private fun checkPhones() = phones.value?.all { it.isValid }

    private fun clearLiveDataValue(){
        listLiveData.forEach {
            it.value = null
        }
    }

    private fun getOrderModel() = OrderRequest(
        cartId = cartId.value ?: DEFAULT_INTEGER_NO_VALUE,
        address = deliveryAddress.value ?: "",
        phones = phones.value ?: emptyList(),
        lastName = lastName.value,
        comment = comments.value,
        firstName = firstName.value ?: "",
        geolocation = map.value ?: "",
        paymentMethod = radioBtnItem.value ?: "",
        orderNumber = orderNumber
    )

    fun setOrdering() = getViewModelScope {
        val response = drugRepo.setOrdering(getOrderModel())
        if (!response.hasBody()) return@getViewModelScope
        if (response!!.isSuccess()){
            clearLiveDataValue()
            _state.value = State.SuccessState.SuccessObjectState(response.body())
        }
    }

    fun getUser() = getViewModelScope {
        val response = drugRepo.getUser()
        if (!response.hasBody()) return@getViewModelScope
        if (response!!.isSuccess()){
            if (firstName.value.isNullOrBlank()){
                firstName.value = response.body()?.firstName
            }
            if (lastName.value.isNullOrBlank()){
                lastName.value = response.body()?.lastName
            }
            if (deliveryAddress.value.isNullOrBlank()){
                response.body()?.address.let { deliveryAddress.value = it }
            }
            if (phones.value.isNullOrEmpty()){
                phones.value = arrayListOf(PhonesItem(response.body()!!.phone))
            }
            _state.value = State.SuccessState.SuccessObjectState(response.body())
        }
    }

    fun payWithElsom(orderID: Int) {
        viewModelScope.launch {
            _progressBarVisibility.postValue(true)

            val result = drugRepo.payWithElsom(orderID)
            when (result) {
                is Result.Data -> {
                    elsomLink = result.data.link

                    _redirectToElsom.postValue(true)
                }
                is Result.Error -> {
                    _snackbarMessage.postValue(R.string.elsom_error_message)
                    _toPopUpNavigation.value = true
                }
            }
            _progressBarVisibility.postValue(false)
        }
    }

    var elsomLink: String? = null
    private var _redirectToElsom = MutableLiveData<Boolean>(false)
    val redirectToElsom: LiveData<Boolean>
        get() = _redirectToElsom

    fun redirectedToElsom() {
        elsomLink = null
        _redirectToElsom.value = false
        _toPopUpNavigation.value = true
    }

    private var _toPopUpNavigation = MutableLiveData<Boolean>(false)
    val toPopUpNavigation: LiveData<Boolean>
        get() = _toPopUpNavigation

    fun popUpedNavigation() {
        _toPopUpNavigation.value = false
    }

    private val _snackbarMessage = MutableLiveData<Int?>()
    val snackbarMessage: LiveData<Int?>
        get() = _snackbarMessage

    fun clearSnackbarMessage() {
        _snackbarMessage.postValue(null)
    }
}