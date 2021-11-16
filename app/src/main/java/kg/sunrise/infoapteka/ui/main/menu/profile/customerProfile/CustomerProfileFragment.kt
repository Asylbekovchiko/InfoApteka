package kg.sunrise.infoapteka.ui.main.menu.profile.customerProfile


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.esafirm.imagepicker.features.ImagePickerConfig
import com.esafirm.imagepicker.features.ImagePickerLauncher
import com.esafirm.imagepicker.features.ImagePickerMode
import com.esafirm.imagepicker.features.registerImagePicker
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.fragment.BaseFragmentWithVM
import kg.sunrise.infoapteka.databinding.FragmentProfileCustomerBinding
import kg.sunrise.infoapteka.networking.models.response.AvatarResponse
import kg.sunrise.infoapteka.networking.models.response.CustomerResponse
import kg.sunrise.infoapteka.networking.models.response.CustomerUpdateResponse
import kg.sunrise.infoapteka.networking.models.response.ProductResponse
import kg.sunrise.infoapteka.ui.auth.city.CitiesBottomSheetFragment
import kg.sunrise.infoapteka.ui.auth.city.CityAdapterDelegate
import kg.sunrise.infoapteka.ui.auth.city.CityRadioBtnItem
import kg.sunrise.infoapteka.ui.main.menu.profile.ProfileViewModel
import kg.sunrise.infoapteka.ui.main.menu.profile.adapter.ProfileAdapterDelegate
import kg.sunrise.infoapteka.ui.main.menu.profile.adapter.ProfileDateDelegate
import kg.sunrise.infoapteka.ui.main.menu.profile.bottomSheetDialog.ChangeProfilePhotoBottomSheetFragment
import kg.sunrise.infoapteka.ui.main.menu.profile.bottomSheetDialog.UpdateAvatarImage
import kg.sunrise.infoapteka.ui.main.menu.profile.sellerProfile.SellerProfileFragment
import kg.sunrise.infoapteka.utils.extensions.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class CustomerProfileFragment :
    BaseFragmentWithVM<FragmentProfileCustomerBinding, ProfileViewModel>(),
    CityAdapterDelegate, ProfileAdapterDelegate, ProfileDateDelegate, UpdateAvatarImage {

    override val viewModel: ProfileViewModel by viewModel()
    override val progressBar: ConstraintLayout by lazy {
        binding.inclProgress.clProgress
    }
    private var typeImage = SellerProfileFragment.ImageTypeForCrop.EmptyImage
    private lateinit var imagePickerLauncher: ImagePickerLauncher
    private var cityBottomSheetFragment: CitiesBottomSheetFragment? = null

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProfileCustomerBinding {
        return FragmentProfileCustomerBinding.inflate(inflater)
    }

    override fun init() {
        setUpUI()
        initToolbar()
        initListeners()
    }

    override fun makeRequests() {
        viewModel.getCustomer()
        viewModel.getCities()
    }

    override fun findTypeOfObject(data: Any?) {
        when (data) {
            is CustomerResponse -> setUpUser(data)
            is CustomerUpdateResponse -> updateCustomer()
            is AvatarResponse -> setUpAvatar(data.avatar)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenCreated {
            imagePickerLauncher = registerImagePicker { listOfImages ->
                listOfImages.forEach { singleImage ->
                    updatePhoto(singleImage.uri)
                }
            }
        }
    }

    private fun updatePhoto(uri: Uri) {
        typeImage = SellerProfileFragment.ImageTypeForCrop.ProfileImage
        CropImage.activity(uri).start(requireContext(), this)
    }

    private fun updateCustomer() {
        snackbar(requireView(), R.string.profile_successfully_updated)
        viewModel.wasAnyChange.value = false
    }

    private fun setUpUser(data: CustomerResponse) {

        binding.tivPhone.setLabel(getString(R.string.phone_number))
        binding.tivPhone.setFullPhoneNumber(data.phone)
        setImageWithPlaceHolder(
            data.avatar.toString(),
            binding.inclAvatar.shivPhoto,
            R.drawable.ic_empty_avatar
        )
        binding.tivFirstName.setInputText(data.firstName)
        binding.tivLastName.setInputText(data.lastName)
        binding.tivPatronymic.setInputText(data.middleName ?: "")
        binding.tivChooseCity.setInputText(data.city.title ?: "")
        binding.tivBirthDate.setInputText(data.birthDate)
        setUpObservables(data)
    }

    private fun setUpUI() {
        binding.tivPhone.setLayoutEnabled(false)
    }

    override fun successRequest() {
        snackbar(requireView(), R.string.avatar_successfully_updated)
    }


    private fun initToolbar() {
        binding.inclToolbar.tvToolbar.text = getString(R.string.Profile)
    }

    private fun initListeners() {
        binding.inclAvatar.root.setOnClickListener {
            ChangeProfilePhotoBottomSheetFragment(this) {
                deleteImage()
            }.show(parentFragmentManager, null)
        }

        binding.inclBtnSave.btnSave.setOnClickListener {
            viewModel.updateCustomer(viewModel.getBuyerModel())
        }

        binding.inclToolbar.ivBack.setOnClickListener { findNavController().navigateUp() }

        binding.tivFirstName.addTextChangedListener {
            viewModel.firstName.value = it.text.toString()
            viewModel.wasAnyChange.value = true
        }
        binding.tivLastName.addTextChangedListener {
            viewModel.lastName.value = it.text.toString()
            viewModel.wasAnyChange.value = true
        }
        binding.tivPatronymic.addTextChangedListener {
            viewModel.patronymic.value = it.text.toString()
            viewModel.wasAnyChange.value = true
        }
        binding.tivChooseCity.setOnLayoutClickListener { chooseCity() }
        binding.tivBirthDate.setOnLayoutClickListener { initDatePicker(this, requireContext()) }
    }

    private fun setUpAvatar(avatar: String?) {
        setImageWithPlaceHolder(
            avatar.toString(),
            binding.inclAvatar.shivPhoto,
            R.drawable.ic_empty_avatar
        )
        snackbar(requireView(), R.string.profile_successfully_updated)
    }

    private fun chooseCity() {
        cityBottomSheetFragment = CitiesBottomSheetFragment(
            this,
            viewModel.cityList
        )
        cityBottomSheetFragment?.show(childFragmentManager, null)
    }

    private fun deleteImage() {
        viewModel.deleteAvatar()
        binding.inclAvatar.shivPhoto.setImageResource(R.drawable.ic_empty_avatar)
    }

    private fun setUpObservables(data: CustomerResponse) {

        viewModel.apply {

            phone.value = data.phone
            firstName.value = data.firstName
            lastName.value = data.lastName
            patronymic.value = data.middleName
            date.value = data.birthDate
            wasAnyChange.value = false
            city.value = data.city.id

            isValid.observe(viewLifecycleOwner) {
                if (it == true) {
                    setEnableBtn()
                } else {
                    setDisableBtn()
                }
            }
        }
    }

    private fun setDisableBtn() {
        binding.inclBtnSave.btnSave.setDisabled(R.color.secondary_gray)
    }

    private fun setEnableBtn() {
        binding.inclBtnSave.btnSave.setEnabled(R.color.green)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            val resultUri = CropImage.getActivityResult(data).uri
            setAvatarImage(resultUri)
        }
    }

    private fun setAvatarImage(uri: Uri) {
        setImageWithPlaceHolder(
            uri.toString(),
            binding.inclAvatar.shivPhoto,
            R.drawable.ic_empty_avatar
        )
        viewModel.setAvatar(uri)
    }

    override fun updateDateClick(date: String) {
        binding.tivBirthDate.setInputText(date)
        viewModel.date.value = date
        viewModel.wasAnyChange.value = true
    }

    override fun onCityItemClicked(item: CityRadioBtnItem, position: Int) {
        binding.tivChooseCity.setInputText(item.title)
        viewModel.city.value = item.id
        viewModel.cityList.forEach { it.isChecked = false }
        item.isChecked = true
        viewModel.wasAnyChange.value = true
        cityBottomSheetFragment?.dismiss()
        cityBottomSheetFragment = null
    }

    override fun onIsNotAvailableClick(response: ProductResponse) {
    }

    override fun launchImagePicker() {
        imagePickerLauncher.launch(ImagePickerConfig(mode = ImagePickerMode.SINGLE))
    }

}
