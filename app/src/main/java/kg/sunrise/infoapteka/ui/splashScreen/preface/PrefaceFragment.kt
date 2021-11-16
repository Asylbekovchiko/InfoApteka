package kg.sunrise.infoapteka.ui.splashScreen.preface

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kg.sunrise.infoapteka.BuildConfig
import kg.sunrise.infoapteka.base.fragment.BaseFragmentWithVM
import kg.sunrise.infoapteka.databinding.FragmentPrefaceBinding
import kg.sunrise.infoapteka.networking.models.response.AppVersion
import kg.sunrise.infoapteka.ui.shared.updateFragment.UpdateBottomSheetFragment
import kg.sunrise.infoapteka.utils.constants.PREFACE_ANIMATION_DURATION
import kg.sunrise.infoapteka.utils.constants.PREFACE_DURATION
import kg.sunrise.infoapteka.utils.extensions.navigateToMain
import kg.sunrise.infoapteka.utils.preference.isUserEnterFirstTime
import kg.sunrise.infoapteka.utils.preference.setUserEntered
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PrefaceFragment : BaseFragmentWithVM<FragmentPrefaceBinding, PrefaceViewModel>() {

    override val viewModel: PrefaceViewModel by viewModel()
    override val progressBar: ConstraintLayout by lazy { binding.inclProgress.clProgress }

    override var isToShowProgress: Boolean = false

    override fun makeRequests() {
        viewModel.getAppVersion()
    }

    override fun findTypeOfObject(data: Any?) {
        when (data) {
            is AppVersion -> {
                checkAppVersion(data)
            }
        }
    }

    private fun checkAppVersion(data: AppVersion) {
        if (data.androidVersion > BuildConfig.VERSION_NAME) {
            val bottomSheet = UpdateBottomSheetFragment(data.androidVersion, data.androidForceUpdate) {
                viewModel.isVersionValid = true
                navigateFurther()
            }
            bottomSheet.show(childFragmentManager, null)
        } else {
            viewModel.isVersionValid = true
            navigateFurther()
        }
    }

    override fun successRequest() {
    }

    override fun inflateView(
            inflater: LayoutInflater,
            container: ViewGroup?
    ): FragmentPrefaceBinding {
        return FragmentPrefaceBinding.inflate(inflater)
    }

    override fun init() {
        startLogoAnimation()
    }

    private fun startLogoAnimation() {
        val alpha = AlphaAnimation(0f, 1f)
        alpha.duration = PREFACE_ANIMATION_DURATION
        binding.ivLogo.startAnimation(alpha)
        startDelay()
    }

    private fun startDelay() {
        lifecycleScope.launch {
            delay(PREFACE_DURATION)
            viewModel.isPrefaceEnd = true
            navigateFurther()
        }
    }

    private fun navigateFurther() {
        if (!viewModel.isVersionValid || !viewModel.isPrefaceEnd) {
            return
        } else if (isUserEnterFirstTime(requireContext())) {
            setUserEntered(requireContext())
            val action = PrefaceFragmentDirections.actionPrefaceFragmentToOnboardingFragment()
            findNavController().navigate(action)
        } else {
            navigateToMain()
        }

    }
}
