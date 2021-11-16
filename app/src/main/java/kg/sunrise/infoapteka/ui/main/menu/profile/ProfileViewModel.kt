package kg.sunrise.infoapteka.ui.main.menu.profile

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.viewModel.BaseViewModel
import kg.sunrise.infoapteka.networking.models.request.CustomerRequest
import kg.sunrise.infoapteka.networking.models.response.SellerProfileResponse
import kg.sunrise.infoapteka.networking.models.response.SellerProfileUpdate
import kg.sunrise.infoapteka.networking.models.response.SertficateResponse
import kg.sunrise.infoapteka.networking.repositories.ProfileRepository
import kg.sunrise.infoapteka.ui.auth.city.CityRadioBtnItem
import kg.sunrise.infoapteka.ui.auth.city.mapToCityRadioBtnItem
import kg.sunrise.infoapteka.ui.main.menu.profile.adapter.MenuProfileOption
import kg.sunrise.infoapteka.utils.enums.ProfileInfoType
import kg.sunrise.infoapteka.utils.extensions.parseListUri
import kg.sunrise.infoapteka.utils.extensions.parseUriToFile
import kg.sunrise.infoapteka.utils.network.State

class ProfileViewModel(private val profileRepo: ProfileRepository) : BaseViewModel() {
    val phone = MutableLiveData<String>()
    val firstName = MutableLiveData<String>()
    val lastName = MutableLiveData<String>()
    val patronymic = MutableLiveData<String>()
    val city = MutableLiveData<Int>()
    val address = MutableLiveData<String>()
    val date = MutableLiveData<String>()
    val wasAnyChange = MutableLiveData(false)
    var cityList = arrayListOf<CityRadioBtnItem>()
    var middleName = MutableLiveData<String>()
    var isAddressNeeded = false

    private val listLiveData = arrayListOf(phone, firstName, lastName, patronymic, city, address, date, wasAnyChange)

    var sellerProfileData : SellerProfileResponse ?= null

    fun getAvatar() = getViewModelScope {
        val response = profileRepo.getAvatar()
        if (!response.hasBody())
            return@getViewModelScope
        if (response!!.isSuccess())
            _state.value = State.SuccessState.SuccessObjectState(response.body())
    }

    fun setAvatar(uri: Uri) = getViewModelScope {
        val response = profileRepo.setAvatar(parseUriToFile(uri))
        if (!response.hasBody())
            return@getViewModelScope
        if (response!!.isSuccess())
            _state.value = State.SuccessState.NoItemState
    }

    fun deleteAvatar() = getViewModelScope {
        val response = profileRepo.deleteAvatar()
        if (!response.hasBody()) return@getViewModelScope
        if (response!!.isSuccess()) {
            _state.value = State.SuccessState.NoItemState
        }
    }

    fun setCertificates(images: ArrayList<Uri>) = getViewModelScope {
        val response = profileRepo.setCertificates(image = parseListUri(images))
        if (!response.hasBody())
            return@getViewModelScope
        if (response!!.isSuccess())
            _state.value = State.SuccessState.SuccessObjectState(response.body())
    }

    fun setUpdateProfileSeller(sellerProfileUpdate: SellerProfileUpdate) = getViewModelScope {
        val response = profileRepo.setUpdateProfileSeller(sellerProfileUpdate)
        if (!response.hasBody())
            return@getViewModelScope
        if (response!!.isSuccess())
            _state.value = State.SuccessState.NoItemState
    }


    fun getCities() = getViewModelScope {
        val response = profileRepo.getCities()
        if (!response.hasBody()) return@getViewModelScope
        if (response!!.isSuccess()) {
            cityList.clear()
            cityList = ArrayList(response.body()!!.map {
                it.mapToCityRadioBtnItem()
            })
            _state.value = State.SuccessState.SuccessObjectState(cityList)
        }
    }

    fun getCustomer() = getViewModelScope {
        val response = profileRepo.getCustomer()
        if (!response.hasBody()) return@getViewModelScope
        if (response!!.isSuccess()) {
            _state.value = State.SuccessState.SuccessObjectState(response.body())
        }
    }

    fun updateCustomer(data: CustomerRequest) = getViewModelScope {
        val response = profileRepo.updateCustomer(data)
        if (!response.hasBody()) return@getViewModelScope
        if (response!!.isSuccess()) {
            _state.value = State.SuccessState.SuccessObjectState(response.body())
        }
    }


    val isValid = MediatorLiveData<Boolean>().apply {
        listLiveData.forEach {
            addSource(it){
                value = checkRequiredFields()
            }
        }
    }


    fun getSellerModel(): SellerProfileUpdate {
        return SellerProfileUpdate(
            firstName.value,
            lastName.value,
            patronymic.value,
            city.value!!,
            address.value,
            date.value,
            arrayOfAllId
        )
    }

    val arrayOfAllId = arrayListOf<Int>()

    fun setValuesToArrayOfAllId(data: List<SertficateResponse>) {
        for (i in data) {
            arrayOfAllId.add(i.id)
        }
    }

    fun getSellerProfile() = getViewModelScope {
        val response = profileRepo.getSellerProfile()
        if (!response.hasBody())
            return@getViewModelScope
        if (response!!.isSuccess())
            _state.value = State.SuccessState.SuccessObjectState(response.body())
    }

    fun getBuyerModel() = CustomerRequest(
        firstName = firstName.value!!,
        lastName = lastName.value!!,
        middleName = patronymic.value,
        city = city.value!!,
        birthDate = date.value!!
    )

    private fun checkRequiredFields(): Boolean {
        return ( !firstName.value.isNullOrBlank()
                && !lastName.value.isNullOrBlank()
                && city.value != null
                && !date.value.isNullOrBlank()
                && wasAnyChange.value!!
                && if (isAddressNeeded) !address.value.isNullOrBlank() else true)
    }

    val menuOptions = arrayListOf<MenuProfileOption>()

    fun addMenuOptionItem(item: SellerProfileResponse , context : Context): ArrayList<MenuProfileOption> {
        menuOptions.apply {
            add(
                MenuProfileOption.ProfileNumber(
                    context.getString(R.string.phone_number),
                    item.phone ?: "",
                    ProfileInfoType.PHONE
                )
            )
            add(
                MenuProfileOption.ProfileMainItem(
                    context.getString(R.string.profile_name),
                    item.firstName ?: "",
                    ProfileInfoType.FIRST_NAME
                )
            )
            add(
                MenuProfileOption.ProfileMainItem(
                    context.getString(R.string.profile_second_name),
                    item.lastName ?: "",
                    ProfileInfoType.LAST_NAME
                )
            )
            add(
                MenuProfileOption.ProfileMainItem(
                    context.getString(R.string.profile_last_name),
                    item.middleName ?: "",
                    ProfileInfoType.PATRONYMIC
                )
            )
            add(
                MenuProfileOption.ProfileCityItem(
                    context.getString(R.string.profile_choose_city),
                    item.city.title ?: "",
                    ProfileInfoType.CITY
                )
            )
            add(
                MenuProfileOption.ProfileMainItem(
                    context.getString(R.string.profile_adress),
                    item.address ?: "",
                    ProfileInfoType.ADDRESS
                )
            )
            add(
                MenuProfileOption.ProfileBirthDate(
                    context.getString(R.string.profile_birth_date),
                    item.birthDate ?: "",
                    ProfileInfoType.DATE
                )
            )
        }
        return menuOptions
    }

}