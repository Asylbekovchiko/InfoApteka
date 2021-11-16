package kg.sunrise.infoapteka.ui.auth.usertype

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.fragment.BaseFragment
import kg.sunrise.infoapteka.databinding.FragmentUserTypeBinding

class UserTypeFragment : BaseFragment<FragmentUserTypeBinding>() {

    private val args by navArgs<UserTypeFragmentArgs>()

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentUserTypeBinding.inflate(inflater)

    override fun init() {
        initRegistrationLabel()
        initListeners()
    }

    private fun initRegistrationLabel() = with(binding) {
        tvSignTypeAction.text = HtmlCompat.fromHtml(
            "${
                getString(R.string.has_account)
            } <font color=\"#5BAC46\">${getString(R.string.Enter)}</font>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }

    private fun initListeners() = with(binding) {
        cvClient.setOnClickListener {
            navigateToClientRegistrationFragment()
        }

        cvSeller.setOnClickListener {
            navigateToSellerRegistrationFragment()
        }

        tvSignTypeAction.setOnClickListener {
            popUpToLoginFragment()
        }

        inclToolbar.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun navigateToClientRegistrationFragment() {
        val action = UserTypeFragmentDirections
            .actionUserTypeFragmentToClientRegistrationFragment(
                args.phoneNumber
            )
        findNavController().navigate(action)
    }

    private fun navigateToSellerRegistrationFragment() {
        val action = UserTypeFragmentDirections
            .actionUserTypeFragmentToSellerRegistrationFragment(
                args.phoneNumber
            )
        findNavController().navigate(action)
    }

    private fun popUpToLoginFragment() {
        findNavController().navigate(
            R.id.action_userTypeFragment_to_loginFragment
        )
    }
}