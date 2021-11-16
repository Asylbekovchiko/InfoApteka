package kg.sunrise.infoapteka.ui.shared.preorderFragment

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kg.sunrise.infoapteka.base.fragment.BaseBottomSheetDialogFragment
import kg.sunrise.infoapteka.databinding.FragmentPreorderBinding

class PreorderBottomSheet(
    private val pharmacy: String,
    private val address: String,
    private val number: String
) : BaseBottomSheetDialogFragment<FragmentPreorderBinding>() {
    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPreorderBinding {
        return FragmentPreorderBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            tvPharmacy.text = pharmacy
            tvAddress.text = address
            tvNumber.text = number

            ivClose.setOnClickListener {
                dismiss()
            }

            btnCall.setOnClickListener {
                callToPharmacy()
            }
        }
    }

    private fun callToPharmacy() {
        try {
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number)))
        } catch (a: ActivityNotFoundException) {
            return
        }
    }
}