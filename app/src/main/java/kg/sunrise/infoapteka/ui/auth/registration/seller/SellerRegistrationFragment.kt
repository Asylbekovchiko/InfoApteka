package kg.sunrise.infoapteka.ui.auth.registration.seller

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.esafirm.imagepicker.features.registerImagePicker
import com.theartofdev.edmodo.cropper.CropImage
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.fragment.BaseFragmentWithVM
import kg.sunrise.infoapteka.databinding.FragmentSellerRegistrationBinding
import kg.sunrise.infoapteka.enums.UserType
import kg.sunrise.infoapteka.networking.models.response.RegistrationResponse
import kg.sunrise.infoapteka.networking.models.response.SertficateResponse
import kg.sunrise.infoapteka.ui.auth.AuthViewModel
import kg.sunrise.infoapteka.ui.auth.registration.RegistrationFieldsFragment
import kg.sunrise.infoapteka.ui.auth.registration.client.ClientRegistrationFragmentArgs
import kg.sunrise.infoapteka.ui.main.menu.profile.sellerProfile.SellerProfileFragment
import kg.sunrise.infoapteka.ui.main.menu.profile.sellerProfile.adapter.ImageClickOnSertificate
import kg.sunrise.infoapteka.ui.main.menu.profile.sellerProfile.adapter.SellerCertificateAdapter
import kg.sunrise.infoapteka.ui.main.menu.profile.sellerProfile.adapter.SellerCertificateClickListener
import kg.sunrise.infoapteka.utils.constants.TAG_REGISTRATION_FRAGMENT
import kg.sunrise.infoapteka.utils.extensions.setDisabled
import kg.sunrise.infoapteka.utils.extensions.setEnabled
import kg.sunrise.infoapteka.utils.extensions.setHtml
import kg.sunrise.infoapteka.utils.preference.setToken
import kg.sunrise.infoapteka.utils.preference.setUserType
import org.koin.androidx.viewmodel.ext.android.viewModel

class SellerRegistrationFragment :
    BaseFragmentWithVM<FragmentSellerRegistrationBinding, AuthViewModel>(),
    SellerCertificateClickListener {

    override val viewModel: AuthViewModel by viewModel()
    override val progressBar: ConstraintLayout by lazy {
        binding.inclProgress.clProgress
    }
    private val listOfUri = arrayListOf<Uri>()

    private val args by navArgs<ClientRegistrationFragmentArgs>()
    private var isRequiredFieldsFilled = false
    private var typeImage = SellerProfileFragment.ImageTypeForCrop.EmptyImage
    private val sellerCertificateAdapter by lazy {
        SellerCertificateAdapter(this)
    }

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSellerRegistrationBinding.inflate(inflater)

    override fun init() {
        initViews()
        initListeners()
    }

    private fun initViews() {
        initLoyaltyRules()
        initRegistrationFields()
        initRV()
    }

    private fun initRV() = with(binding.rvItemsCertificate) {
        adapter = sellerCertificateAdapter
    }

    private fun initLoyaltyRules() = with(binding.tvLoyalty) {
        setHtml(
            getString(
                R.string.loyalty_rules_pattern,
                "<font color=\"#0098D7\">${
                    getString(R.string.loyalty_rules)
                }</font>"
            )
        )
    }

    private fun initRegistrationFields() {
        val fragment = childFragmentManager
            .findFragmentByTag(TAG_REGISTRATION_FRAGMENT)
            ?: RegistrationFieldsFragment(
                true, args.phoneNumber,
                this::onRegistrationFieldChanged, viewModel
            )
        childFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container_view, fragment).commit()
        }
    }

    private fun onRegistrationFieldChanged(
        isRequiresFilled: Boolean
    ) = with(binding) {
        isRequiredFieldsFilled = isRequiresFilled
        setRegistrationButtonEnabling()
    }

    private fun setRegistrationButtonEnabling() = with(binding) {
        if (isRequiredFieldsFilled && cbLoyalty.isChecked
        ) {
            btnSignUp.setEnabled(R.color.green)
        } else {
            btnSignUp.setDisabled(R.color.secondary_gray)
        }
    }

    private fun initListeners() = with(binding) {
        inclToolbar.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        cbLoyalty.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked && isRequiredFieldsFilled) {
                btnSignUp.setEnabled(R.color.green)
            } else {
                btnSignUp.setDisabled(R.color.secondary_gray)
            }
        }

        btnSignUp.setOnClickListener {
            viewModel.sellerRegistration()
        }

        tvLoyalty.setOnClickListener {
            findNavController().navigate(R.id.action_global_privacyPolicyFragment)
        }

        initClickForCertificateItem()
    }

    private fun initClickForCertificateItem() {
        val launcher = registerImagePicker {
            typeImage = SellerProfileFragment.ImageTypeForCrop.SertificateImage
            it.forEach { i ->
                CropImage.activity(i.uri).start(
                    requireContext(), this@SellerRegistrationFragment
                )
            }
        }
        binding.loadFileImageView.setClickListenerOnUpload {
            launcher.launch()
        }
    }

    override fun makeRequests() {
        viewModel.getCities()
    }

    override fun findTypeOfObject(data: Any?) {
        if ((data as? ArrayList<SertficateResponse>) != null) {
            navigateToRegistrationSuccess()
        } else if (data is RegistrationResponse) {
            onRegistrationResponse(data)
        }
    }

    private fun onRegistrationResponse(
        registrationResponse: RegistrationResponse
    ) = with(registrationResponse) {
        setToken(requireContext(), token)
        setUserType(requireContext(), UserType.SELLER)
        if (sellerCertificateAdapter.getAllItems().isEmpty()) {
            navigateToRegistrationSuccess()
        } else {
            viewModel.postCertificates(
                listOfUri
            )
        }
    }

    private fun navigateToRegistrationSuccess() {
        val action = R.id.action_sellerRegistrationFragment_to_registrationSuccessfulFragment
        findNavController().navigate(
            action
        )
    }

    override fun successRequest() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val uri = result.uri
                when (typeImage) {
                    SellerProfileFragment.ImageTypeForCrop.SertificateImage -> {
                        sellerCertificateAdapter.addData(arrayListOf(SertficateResponse(image = uri.toString())))
                        setRegistrationButtonEnabling()
                        listOfUri.add(uri)
                    }
                    else -> {
                        throw IllegalStateException(result.error)
                    }

                }
            }
        }
        setRegistrationButtonEnabling()
    }

    override fun onClickListener(
        position: Int, type: ImageClickOnSertificate
    ) {
        when (type) {
            ImageClickOnSertificate.RedCross -> {
                sellerCertificateAdapter.deletedItemUpdate(position)
                setRegistrationButtonEnabling()
            }
            ImageClickOnSertificate.ImageZoom ->
                zoomCertificate(position)
        }
    }

    private fun zoomCertificate(position: Int) {
        val uri =
            sellerCertificateAdapter.getAt(position).image
        val action = SellerRegistrationFragmentDirections
            .actionSellerRegistrationFragmentToZoomFragment(
                uri
            )
        findNavController().navigate(action)
    }
}