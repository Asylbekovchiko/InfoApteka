package kg.sunrise.infoapteka.ui.auth.registration

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kg.sunrise.infoapteka.base.fragment.BaseFragment
import kg.sunrise.infoapteka.databinding.FragmentRegistrationFieldsBinding
import kg.sunrise.infoapteka.networking.models.request.UserRequest
import kg.sunrise.infoapteka.ui.auth.AuthViewModel
import kg.sunrise.infoapteka.ui.auth.city.CitiesBottomSheetFragment
import kg.sunrise.infoapteka.ui.auth.city.CityAdapterDelegate
import kg.sunrise.infoapteka.ui.auth.city.CityRadioBtnItem
import kg.sunrise.infoapteka.ui.main.menu.profile.adapter.ProfileDateDelegate
import kg.sunrise.infoapteka.utils.extensions.initDatePicker
import kg.sunrise.infoapteka.utils.network.State
import kg.sunrise.infoapteka.utils.parsers.DateTimeParser
import kg.sunrise.infoapteka.utils.parsers.DateTimePattern
import java.util.*

class RegistrationFieldsFragment(
    private val isSeller: Boolean,
    private val phoneNumber: String,
    private val onChangedListener: (Boolean) -> Unit,
    private val viewModel: AuthViewModel
) : BaseFragment<FragmentRegistrationFieldsBinding>(),
    ProfileDateDelegate, CityAdapterDelegate {

    private var cityBottomSheetFragment: CitiesBottomSheetFragment? = null
    private var cities = arrayListOf<CityRadioBtnItem>()

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentRegistrationFieldsBinding.inflate(inflater)

    override fun init() {
        initViews()
        initObservers()
        initListeners()
    }

    private fun initViews() = with(binding) {
        etAddress.visibility =
            if (isSeller) View.VISIBLE
            else View.GONE
        etCity.setLayoutEnabled(false)
        etBirthday.setLayoutEnabled(false)
        etPhoneNumber.setLayoutEnabled(false)
        etPhoneNumber.setFullPhoneNumber(phoneNumber)
    }

    private fun getBirthdayWithDots(givenBirthday: String): String {
        if (givenBirthday.isBlank()) return givenBirthday
        return givenBirthday
    }

    private fun initObservers() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            if (state is State.SuccessState.SuccessListState<*>
                && state.data.all { it is CityRadioBtnItem }
            ) {
                cities = (state.data as ArrayList<CityRadioBtnItem>)
                initFields()
            }
        }
    }

    private fun initFields() = with(binding) {
        viewModel.userRequest?.let {
            etFirstName.setInputText(it.firstName)
            etLastName.setInputText(it.lastName)
            etMiddleName.setInputText(it.middleName ?: "")
            etCity.setInputText(cities.find { city ->
                city.id == it.city
            }?.title ?: "")
            etAddress.setInputText(it.address ?: "")
            etBirthday.setInputText(getBirthdayWithDots(it.birthday))
        }
    }

    private fun initListeners() = with(binding) {
        etCity.setOnLayoutClickListener {
            chooseCity()
        }

        etBirthday.setOnLayoutClickListener {
            initDatePicker(this@RegistrationFieldsFragment, requireContext())
        }

        listOf(
            etFirstName, etLastName, etMiddleName,
            etCity, etAddress, etBirthday
        ).forEach {
            it.addTextChangedListener { onChangedListener() }
        }
    }

    private fun chooseCity() {
        cityBottomSheetFragment = CitiesBottomSheetFragment(
            this,
            cities
        )
        cityBottomSheetFragment?.show(childFragmentManager, null)
    }

    private fun onChangedListener() {
        viewModel.userRequest = generateUserRequest()
        onChangedListener(isAllFilled())
    }

    private fun isAllFilled(): Boolean = with(binding) {
        with(etFirstName.getInputText()) {
            isNotBlank() && isNotEmpty()
        } && with(etLastName.getInputText()) {
            isNotBlank() && isNotEmpty()
        } && with(etCity.getInputText()) {
            isNotBlank() && isNotEmpty()
        } && with(etAddress) {
            if (isSeller)
                getInputText().isNotEmpty() && getInputText().isNotBlank()
            else true
        } && with(etBirthday.getInputText()) {
            isNotBlank() && isNotEmpty()
        }
    }

    private fun generateUserRequest() = UserRequest(
        binding.etPhoneNumber.getPhone(),
        binding.etFirstName.getInputText(),
        with(binding.etMiddleName.getInputText()) {
            if (isBlank()) null else this
        },
        binding.etLastName.getInputText(),
        cities.find {
            it.title == binding.etCity.getInputText()
        }?.id ?: -1,
        if (isSeller) binding.etAddress.getInputText() else null,
        getBirthday()
    )

    private fun getBirthday(): String {
        val birthday = binding.etBirthday.getInputText()
        if (birthday.isBlank()) return birthday
        return birthday
    }

    override fun updateDateClick(date: String) {
        binding.etBirthday.setInputText(date)
    }

    override fun onCityItemClicked(
        item: CityRadioBtnItem,
        position: Int
    ) = with(item) {
        cities.forEach { it.isChecked = false }
        item.isChecked = true
        binding.etCity.setInputText(title)
        cityBottomSheetFragment?.dismiss()
        cityBottomSheetFragment = null
    }
}