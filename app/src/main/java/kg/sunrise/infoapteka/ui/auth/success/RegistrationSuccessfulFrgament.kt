package kg.sunrise.infoapteka.ui.auth.success

import android.view.LayoutInflater
import android.view.ViewGroup
import kg.sunrise.infoapteka.base.fragment.BaseFragment
import kg.sunrise.infoapteka.databinding.FragmentRegistrationSuccessfulBinding
import kg.sunrise.infoapteka.utils.extensions.navigateToMain

class RegistrationSuccessfulFragment :
    BaseFragment<FragmentRegistrationSuccessfulBinding>() {

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentRegistrationSuccessfulBinding.inflate(inflater)

    override fun init() {
        initListeners()
    }

    private fun initListeners() = with(binding) {
        btnToMain.setOnClickListener {
            requireActivity().navigateToMain()
        }
    }
}