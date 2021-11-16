package kg.sunrise.infoapteka.ui.auth.confirm

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.fragment.BaseFragmentWithVM
import kg.sunrise.infoapteka.databinding.FragmentConfirmBinding
import kg.sunrise.infoapteka.enums.UserType
import kg.sunrise.infoapteka.networking.models.response.AuthResponse
import kg.sunrise.infoapteka.ui.auth.AuthViewModel
import kg.sunrise.infoapteka.utils.PhoneAuth
import kg.sunrise.infoapteka.utils.constants.CONFIRMATION_CODE_LENGTH
import kg.sunrise.infoapteka.utils.extensions.*
import kg.sunrise.infoapteka.utils.preference.DEFAULT_TOKEN_VALUE
import kg.sunrise.infoapteka.utils.preference.setToken
import kg.sunrise.infoapteka.utils.preference.setUserType
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConfirmFragment :
    BaseFragmentWithVM<FragmentConfirmBinding, AuthViewModel>(),
    PhoneConfirmable {

    override val viewModel: AuthViewModel by viewModel()
    override val progressBar: ConstraintLayout by lazy {
        binding.inclProgress.clProgress
    }

    private val args by navArgs<ConfirmFragmentArgs>()
    private val timerViewModel: TimerViewModel by viewModel()
    private val sendAgainFormat by lazy {
        getString(R.string.send_again_in)
    }
    private val phoneAuth by lazy { PhoneAuth(this) }
    private var isCodeSent = false

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentConfirmBinding.inflate(inflater)

    override fun init() {
        initViews()
        initTimerVM()
        initListeners()
    }

    private fun initViews() = with(binding) {
        tvPhoneNumber.text = args.phoneNumber
        etConfirmCode.setInputLength(CONFIRMATION_CODE_LENGTH)
    }

    private fun initTimerVM() {
        timerViewModel.timerLiveData
            .observe(viewLifecycleOwner) { timer ->
                when (timer) {
                    is TimerTick -> {
                        binding.btnResend.text = String.format(
                            sendAgainFormat, timer.value
                        )
                    }

                    is TimerFinish -> {
                        binding.btnResend.apply {
                            text = getString(R.string.send_again)
                            isEnabled = true
                        }
                    }
                }
            }
    }

    private fun initListeners() = with(binding) {
        btnResend.setOnClickListener {
            it.isEnabled = false
            timerViewModel.startTimer()
            binding.inclProgress.clProgress.visible()
            requestConfirmationCode()
        }

        etConfirmCode.addTextChangedListener {
            checkButtonEnabling()
        }

        tvLabelWrongPhoneNumber.setOnClickListener {
            findNavController().navigateUp()
        }

        inclToolbar.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        btnConfirm.setOnClickListener {
            binding.inclProgress.clProgress.visible()
            phoneAuth.checkConfirmCode(etConfirmCode.getInputText())
        }

        btnResend.callOnClick()
    }

    private fun checkButtonEnabling() = with(binding) {
        val confirmCodeLength = etConfirmCode.getInputText().length
        if (confirmCodeLength == CONFIRMATION_CODE_LENGTH
            && isCodeSent
        ) {
            btnConfirm.setEnabled(R.color.green)
        } else {
            btnConfirm.setDisabled(R.color.secondary_gray_2)
        }
    }

    private fun requestConfirmationCode() {
        phoneAuth.requestConfirmCode(
            requireActivity(), args.phoneNumber
        )
    }

    override fun makeRequests() {
    }

    override fun findTypeOfObject(data: Any?) {
        when (data) {
            is AuthResponse -> onAuthResponse(data)
        }
    }

    @SuppressLint("DefaultLocale")
    private fun onAuthResponse(
        authResponse: AuthResponse
    ) = with(authResponse) {
        setToken(requireContext(), token ?: DEFAULT_TOKEN_VALUE)
        setUserType(
            requireContext(), UserType.valueOf(
                role?.toUpperCase() ?: UserType.UNAUTHORIZED.toString()
            )
        )
        requireActivity().navigateToMain()
    }

    override fun successRequest() {
        navigateToUserTypeFragment()
    }

    private fun navigateToUserTypeFragment() {
        val action = ConfirmFragmentDirections
            .actionConfirmFragmentToUserTypeFragment(args.phoneNumber)
        findNavController().navigate(action)
    }

    override fun onVerificationFailed() {
        binding.inclProgress.clProgress.gone()
        snackbar(requireView(), R.string.wrong_confirm_code)
    }

    override fun onCodeSent() {
        binding.inclProgress.clProgress.gone()
        isCodeSent = true
        checkButtonEnabling()
    }

    override fun onSigned() {
        binding.inclProgress.clProgress.gone()
        viewModel.auth(args.phoneNumber)
    }

}