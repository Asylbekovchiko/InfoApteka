package kg.sunrise.infoapteka.ui.shared.updateFragment

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kg.sunrise.infoapteka.BuildConfig
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.fragment.BaseBottomSheetDialogFragment
import kg.sunrise.infoapteka.databinding.FragmentUpdateBottomSheetBinding
import java.lang.Exception

class UpdateBottomSheetFragment(
        private val appVersion: String,
        private val forcedUpdate: Boolean,
        private val onDismiss: () -> Unit
) : BaseBottomSheetDialogFragment<FragmentUpdateBottomSheetBinding>() {

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): FragmentUpdateBottomSheetBinding {
        return FragmentUpdateBottomSheetBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isCancelable = false

        binding.apply {
            binding.tvVersionText.text =getString(R.string.App_update, appVersion)
            binding.btnUpdateLater.visibility = if (forcedUpdate) View.GONE else View.VISIBLE

            btnUpdateLater.setOnClickListener {
                onDismiss()
                dismiss()
            }

            btnUpdate.setOnClickListener {
                updateApp()
            }
        }
    }

    private fun updateApp() {
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}}"
                        )
                )
            )
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=${BuildConfig.APPLICATION_ID}"
                    )
                )
            )
        }
    }
}