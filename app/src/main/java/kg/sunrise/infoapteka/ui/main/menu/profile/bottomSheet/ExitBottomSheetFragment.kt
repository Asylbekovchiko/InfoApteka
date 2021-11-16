package kg.sunrise.infoapteka.ui.main.menu.profile.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kg.sunrise.infoapteka.base.fragment.BaseBottomSheetDialogFragment
import kg.sunrise.infoapteka.databinding.FragmentExitBottomSheetBinding
import kg.sunrise.infoapteka.utils.extensions.signOut


class ExitBottomSheetFragment()
    : BaseBottomSheetDialogFragment<FragmentExitBottomSheetBinding>() {

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentExitBottomSheetBinding {
        return FragmentExitBottomSheetBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        binding.btnExit.setOnClickListener {
            requireActivity().signOut()
        }

        binding.btnDismiss.setOnClickListener {
            dismiss()
        }
    }
}