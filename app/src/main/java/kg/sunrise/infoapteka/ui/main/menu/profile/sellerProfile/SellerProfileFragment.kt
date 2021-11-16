package kg.sunrise.infoapteka.ui.main.menu.profile.sellerProfile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import com.theartofdev.edmodo.cropper.CropImage
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.fragment.BaseFragmentWithVM
import kg.sunrise.infoapteka.databinding.FragmentSellerProfileBinding
import kg.sunrise.infoapteka.networking.models.response.*
import kg.sunrise.infoapteka.ui.auth.city.CitiesBottomSheetFragment
import kg.sunrise.infoapteka.ui.auth.city.CityAdapterDelegate
import kg.sunrise.infoapteka.ui.auth.city.CityRadioBtnItem
import kg.sunrise.infoapteka.ui.main.menu.profile.ProfileViewModel
import kg.sunrise.infoapteka.ui.main.menu.profile.adapter.BaseProfileAdapterOption
import kg.sunrise.infoapteka.ui.main.menu.profile.adapter.ProfileAdapterDelegate
import kg.sunrise.infoapteka.ui.main.menu.profile.adapter.ProfileDateDelegate
import kg.sunrise.infoapteka.ui.main.menu.profile.adapter.ProfileInfo
import kg.sunrise.infoapteka.ui.main.menu.profile.bottomSheetDialog.ChangeProfilePhotoBottomSheetFragment
import kg.sunrise.infoapteka.ui.main.menu.profile.sellerProfile.adapter.ImageClickOnSertificate
import kg.sunrise.infoapteka.ui.main.menu.profile.sellerProfile.adapter.SellerCertificateAdapter
import kg.sunrise.infoapteka.ui.main.menu.profile.sellerProfile.adapter.SellerCertificateClickListener
import kg.sunrise.infoapteka.ui.main.menu.profile.sellerProfile.zoomAlert.ZoomAlertDialog
import kg.sunrise.infoapteka.utils.enums.ProfileInfoType
import kg.sunrise.infoapteka.utils.extensions.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.esafirm.imagepicker.features.*
import kg.sunrise.infoapteka.ui.main.menu.profile.bottomSheetDialog.UpdateAvatarImage
import kotlinx.android.synthetic.main.avatar_shapeable_image_view.view.*
import kotlinx.android.synthetic.main.fragment_profile_customer.view.*


class SellerProfileFragment :
    BaseFragmentWithVM<FragmentSellerProfileBinding, ProfileViewModel>(),
    SellerCertificateClickListener, CityAdapterDelegate, ProfileAdapterDelegate,
    ProfileDateDelegate, ProfileInfo, UpdateAvatarImage {
    private val sellerAdapter by lazy {
        BaseProfileAdapterOption(this)
    }
    private lateinit var imagePickerLauncher: ImagePickerLauncher

    override val viewModel: ProfileViewModel by viewModel()
    private val certificateImages = arrayListOf<Uri>()
    private var countIfItems = 0
    private var countOfRequests = 0
    private var cityBottomSheetFragment: CitiesBottomSheetFragment? = null
    private var typeImage = ImageTypeForCrop.EmptyImage
    private val sellerCertificateAdapter by lazy {
        SellerCertificateAdapter(this)
    }
    override val progressBar: ConstraintLayout by lazy {
        binding.inclProgress.clProgress
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

    override fun makeRequests() {
        viewModel.getAvatar()
        viewModel.getCities()
        if (viewModel.sellerProfileData == null) {
            viewModel.getSellerProfile()
        } else {
            initAllData()
        }
    }

    private fun initAllData() {
        viewModel.sellerProfileData?.let { setUpObservables(it) }
        binding.rvProfileInfo.adapter = sellerAdapter
    }

    override fun findTypeOfObject(data: Any?) {
        when (data) {
            is AvatarResponse -> {
                setImageWithPlaceHolder(
                    data.avatar.toString(),
                    binding.inclAvatar.shivPhoto,
                    R.drawable.ic_empty_avatar
                )
            }
            is ArrayList<*> -> checkArray(data)
            is SellerProfileResponse -> initAdapter(data)
        }
    }

    private fun initAdapter(data: SellerProfileResponse) {
        if (viewModel.sellerProfileData == null) {
            viewModel.sellerProfileData = data
        }
        viewModel.sellerProfileData?.let { setUpObservables(it) }
        binding.rvProfileInfo.adapter = sellerAdapter
        viewModel.sellerProfileData?.let { viewModel.addMenuOptionItem(it, requireContext()) }
        sellerAdapter.setData(viewModel.menuOptions)
        binding.rvItemsCertificate.adapter = sellerCertificateAdapter
        viewModel.setValuesToArrayOfAllId(data.certificates)
        sellerCertificateAdapter.setData(data.certificates)
    }

    private fun checkArray(data: ArrayList<*>) {
        data.filterIsInstance<SertficateResponse>().also { listOfCertificates ->
            viewModel.setValuesToArrayOfAllId(listOfCertificates)
        }
    }


    override fun successRequest() {
        requireContext().snackbar(requireView(), getString(R.string.profile_successfully_updated))
    }

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSellerProfileBinding {
        return FragmentSellerProfileBinding.inflate(inflater)
    }

    override fun init() {
        initToolbar()
        initClickForCertificateItem()
        initAvatar()
        initListeners()
    }

    private fun initListeners() {
        binding.inclBtnSave.btnSave.setOnClickListener {
            viewModel.setUpdateProfileSeller(viewModel.getSellerModel())
            setDisableBtn()
        }

        binding.inclToolbar.ivBack.setOnClickListener { findNavController().navigateUp() }
    }

    private fun initAvatar() {
        binding.inclAvatar.root.setOnClickListener {
            ChangeProfilePhotoBottomSheetFragment(this) {
                deleteAvatarImage()
            }.show(parentFragmentManager, null)
        }
    }

    private fun deleteAvatarImage() {
        viewModel.deleteAvatar()
        binding.inclAvatar.shivPhoto.setImageResource(R.drawable.ic_empty_avatar)
    }

    private fun initClickForCertificateItem() {
        val laucherImagePicker = registerImagePicker {
            typeImage = ImageTypeForCrop.SertificateImage
            it.forEach { i ->
                countIfItems++
                CropImage.activity(i.uri).start(requireContext(), this@SellerProfileFragment)
            }
        }
        binding.loadFileImageView.setClickListenerOnUpload {
            laucherImagePicker.launch()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val uri = result.uri
                when (typeImage) {
                    ImageTypeForCrop.SertificateImage -> {
                        updateCertificatePhotos(uri)
                    }
                    ImageTypeForCrop.ProfileImage -> {
                        updateAvatar(uri)
                    }
                }
            }
        }
    }

    private fun updateAvatar(uri: Uri) {
        setImageWithPlaceHolder(
            uri.toString(),
            binding.clAvatar.shiv_photo,
            R.drawable.ic_empty_avatar
        )
        viewModel.setAvatar(uri)
    }

    private fun updateCertificatePhotos(uri: Uri) {
        sellerCertificateAdapter.addData(arrayListOf(SertficateResponse(image = uri.toString())))
        viewModel.wasAnyChange updateValue true
        countOfRequests++
        certificateImages.add(uri)
        if (countIfItems == countOfRequests)
            viewModel.setCertificates(certificateImages)
    }


    private fun initToolbar() {
        binding.inclToolbar.tvToolbar.text = getString(R.string.Profile)
    }


    enum class ImageTypeForCrop {
        EmptyImage,
        ProfileImage,
        SertificateImage;
    }

    private fun setDisableBtn() {
        binding.inclBtnSave.btnSave.setDisabled(R.color.secondary_gray)
    }

    private fun setEnableBtn() {
        binding.inclBtnSave.btnSave.setEnabled(R.color.green_5BAC46)
    }

    override fun onClickListener(position: Int, type: ImageClickOnSertificate) {
        when (type) {
            ImageClickOnSertificate.RedCross -> {
                deleteImage(position)
            }
            ImageClickOnSertificate.ImageZoom -> {
                ZoomAlertDialog(sellerCertificateAdapter.getAllItems()[position].image).show(
                    parentFragmentManager,
                    null
                )
            }
        }
    }

    private fun deleteImage(position: Int) {
        sellerCertificateAdapter.deletedItemUpdate(position)
        viewModel.arrayOfAllId.removeAt(position)
        viewModel.wasAnyChange updateValue true
    }

    private fun setUpObservables(data: SellerProfileResponse) {
        viewModel.apply {
            phone updateValue data.phone
            firstName updateValue data.firstName
            lastName updateValue data.lastName
            patronymic updateValue data.middleName
            city updateValue data.city.id
            address updateValue data.address
            date updateValue data.birthDate

            isAddressNeeded = true
            isValid.observe(viewLifecycleOwner) {
                if (it == true)
                    setEnableBtn()
                else
                    setDisableBtn()

            }
        }
    }

    private fun chooseCity() {
        cityBottomSheetFragment = CitiesBottomSheetFragment(
            this@SellerProfileFragment,
            viewModel.cityList
        )
        cityBottomSheetFragment?.show(childFragmentManager, null)
    }

    private fun updateDate() {
        context?.let { initDatePicker(this, it) }
    }

    override fun updateDateClick(date: String) {
        sellerAdapter.dateChanged(date)
        viewModel.date updateValue date
    }

    override fun updateProfileInfo(info: String, profileType: ProfileInfoType) {
        viewModel.wasAnyChange.value = true
        viewModel.apply {
            when (profileType) {
                ProfileInfoType.FIRST_NAME -> {
                    firstName updateValue info
                }
                ProfileInfoType.LAST_NAME -> {
                    lastName updateValue info
                }
                ProfileInfoType.PATRONYMIC -> {
                    patronymic updateValue info
                }
                ProfileInfoType.ADDRESS -> {
                    address updateValue info
                }
                ProfileInfoType.DATE -> {
                    date updateValue info
                }
            }
        }
    }

    override fun onItemProfileClick(profileType: ProfileInfoType) {
        when (profileType) {
            ProfileInfoType.CITY -> chooseCity()
            ProfileInfoType.DATE -> updateDate()
        }
    }

    override fun onCityItemClicked(item: CityRadioBtnItem, position: Int) {
        viewModel.wasAnyChange updateValue true
        sellerAdapter.changeCity(item.title)
        viewModel.cityList.forEach { it.isChecked = false }
        item.isChecked = true
        viewModel.city updateValue item.id
        cityBottomSheetFragment?.dismiss()
        cityBottomSheetFragment = null
    }

    override fun onIsNotAvailableClick(response: ProductResponse) {
    }

    private fun updatePhoto(uri: Uri) {
        typeImage = ImageTypeForCrop.ProfileImage
        CropImage.activity(uri).start(requireContext(), this)
    }

    override fun launchImagePicker() {
        imagePickerLauncher.launch(ImagePickerConfig(mode = ImagePickerMode.SINGLE))
    }
}