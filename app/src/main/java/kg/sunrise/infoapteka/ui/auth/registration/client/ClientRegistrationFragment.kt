package kg.sunrise.infoapteka.ui.auth.registration.client

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.fragment.BaseFragmentWithVM
import kg.sunrise.infoapteka.databinding.FragmentClientRegistrationBinding
import kg.sunrise.infoapteka.enums.UserType
import kg.sunrise.infoapteka.networking.models.response.RegistrationResponse
import kg.sunrise.infoapteka.ui.auth.AuthViewModel
import kg.sunrise.infoapteka.ui.auth.registration.RegistrationFieldsFragment
import kg.sunrise.infoapteka.utils.constants.TAG_REGISTRATION_FRAGMENT
import kg.sunrise.infoapteka.utils.extensions.setDisabled
import kg.sunrise.infoapteka.utils.extensions.setEnabled
import kg.sunrise.infoapteka.utils.extensions.setHtml
import kg.sunrise.infoapteka.utils.preference.setToken
import kg.sunrise.infoapteka.utils.preference.setUserType
import org.koin.androidx.viewmodel.ext.android.viewModel

class ClientRegistrationFragment :
    BaseFragmentWithVM<FragmentClientRegistrationBinding, AuthViewModel>() {

    override val viewModel: AuthViewModel by viewModel()
    override val progressBar: ConstraintLayout by lazy {
        binding.inclProgress.clProgress
    }

    private val args by navArgs<ClientRegistrationFragmentArgs>()
    private var isRequiredFieldsFilled = false

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentClientRegistrationBinding.inflate(inflater)

    override fun init() {
        initViews()
        initListeners()
    }

    private fun initViews() {
        initLoyaltyRules()
        initRegistrationFields()
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
                false, args.phoneNumber,
                this::onRegistrationFieldChanged, viewModel
            )
        childFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container_view, fragment).commit()
        }
    }

    private fun onRegistrationFieldChanged(
        isRequiresFilled: Boolean,
    ) = with(binding) {
        isRequiredFieldsFilled = isRequiresFilled
        if (cbLoyalty.isChecked && isRequiresFilled) {
            btnSignUp.setEnabled(R.color.green)
        } else {
            btnSignUp.setDisabled(R.color.secondary_gray)
        }
    }

    private fun initListeners() = with(binding) {
        inclToolbar.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        btnSignUp.setOnClickListener {
            viewModel.clientRegistration()
        }

        cbLoyalty.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked && isRequiredFieldsFilled) {
                btnSignUp.setEnabled(R.color.green)
            } else {
                btnSignUp.setDisabled(R.color.secondary_gray)
            }
        }

        tvLoyalty.setOnClickListener {
            findNavController().navigate(R.id.action_global_privacyPolicyFragment)
        }
    }

    override fun makeRequests() {
        viewModel.getCities()
    }

    override fun findTypeOfObject(data: Any?) {
        when (data) {
            is RegistrationResponse -> onRegistrationResponse(data)
        }
    }

    private fun onRegistrationResponse(
        registrationResponse: RegistrationResponse
    ) = with(registrationResponse) {
        setToken(requireContext(), token)
        setUserType(requireContext(), UserType.CLIENT)
        findNavController().navigate(
            R.id.action_clientRegistrationFragment_to_registrationSuccessfulFragment
        )
    }

    override fun successRequest() {
    }
}