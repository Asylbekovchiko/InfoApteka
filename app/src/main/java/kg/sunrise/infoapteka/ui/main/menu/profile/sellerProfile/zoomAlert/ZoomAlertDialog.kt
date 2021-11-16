package kg.sunrise.infoapteka.ui.main.menu.profile.sellerProfile.zoomAlert

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import kg.sunrise.infoapteka.base.fragment.BaseAlertDialogFragment
import kg.sunrise.infoapteka.databinding.AlertZoomCertificateBinding

class ZoomAlertDialog(private val uri : String) :
    BaseAlertDialogFragment<AlertZoomCertificateBinding>() {
    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): AlertZoomCertificateBinding {
        return AlertZoomCertificateBinding.inflate(inflater)
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(requireContext()).load(uri).into(binding.ivZoomImage)
        binding.inclToolbar.ivBack.setOnClickListener {
            dismiss()
        }
    }

}