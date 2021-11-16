package kg.sunrise.infoapteka.ui.splashScreen.onboarding.onboardingSteps

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kg.sunrise.infoapteka.base.fragment.BaseFragment
import kg.sunrise.infoapteka.databinding.FragmentOnboardingPageBinding
import kg.sunrise.infoapteka.utils.extensions.setTextColorRes

class OnboardingPageFragment(
    @StringRes val textRes: Int,
    @DrawableRes val drawableRes: Int,
    @ColorRes val backgroundRes: Int,
    @ColorRes val textColorRes: Int,
    val delegate: OnboardingPageDelegate
): BaseFragment<FragmentOnboardingPageBinding>() {

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentOnboardingPageBinding {
        return FragmentOnboardingPageBinding.inflate(inflater)
    }

    override fun init() {
        binding.clParent.setBackgroundResource(backgroundRes)
        binding.ivIcon.setImageResource(drawableRes)
        binding.tvContent.setText(textRes)
        binding.tvSkip.setTextColorRes(textColorRes)
        binding.tvContent.setTextColorRes(textColorRes)

        binding.tvSkip.setOnClickListener {
            delegate.onSkipClicked()
        }
    }
}

interface OnboardingPageDelegate {

    fun onSkipClicked()
}