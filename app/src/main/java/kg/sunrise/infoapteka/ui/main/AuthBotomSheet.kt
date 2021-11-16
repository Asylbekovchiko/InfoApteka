package kg.sunrise.infoapteka.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import kg.sunrise.infoapteka.base.fragment.BaseBottomSheetDialogFragment
import kg.sunrise.infoapteka.databinding.BotomSheetFragmentAuthBinding
import kg.sunrise.infoapteka.utils.extensions.navigateToAuth
import kg.sunrise.infoapteka.utils.extensions.navigateToMain

class AuthBotomSheet : BaseBottomSheetDialogFragment<BotomSheetFragmentAuthBinding>() {

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): BotomSheetFragmentAuthBinding {
        return BotomSheetFragmentAuthBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnDismiss.setOnClickListener { dismiss() }
            btnEnter.setOnClickListener {
                navigateToAuth()
                dismiss()
            }
        }
    }
}