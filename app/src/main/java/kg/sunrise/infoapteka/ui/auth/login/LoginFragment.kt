package kg.sunrise.infoapteka.ui.auth.login

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.fragment.BaseFragment
import kg.sunrise.infoapteka.databinding.FragmentLoginBinding
import kg.sunrise.infoapteka.utils.extensions.setDisabled
import kg.sunrise.infoapteka.utils.extensions.setEnabled

class LoginFragment :
    BaseFragment<FragmentLoginBinding>() {

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLoginBinding.inflate(inflater)

    override fun init() {
        initListeners()
    }

    private fun initListeners() = with(binding) {
        etPhoneNumber.isPhoneNumberValid.observe(viewLifecycleOwner) {
            if (it) btnSignIn.setEnabled(R.color.green)
            else btnSignIn.setDisabled(R.color.secondary_gray)
        }

        btnSignIn.setOnClickListener {
            navigateToConfirmFragment()
        }

        inclToolbar.ivBack.setOnClickListener {
            requireActivity().finish()
        }
    }

    private fun navigateToConfirmFragment() {
        val action = LoginFragmentDirections
            .actionLoginFragmentToConfirmFragment(
                binding.etPhoneNumber.getPhone(),
            )
        findNavController().navigate(action)
    }

}