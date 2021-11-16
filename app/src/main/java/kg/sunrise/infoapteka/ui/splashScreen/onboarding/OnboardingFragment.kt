package kg.sunrise.infoapteka.ui.splashScreen.onboarding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.navigation.fragment.findNavController
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.fragment.BaseFragment
import kg.sunrise.infoapteka.databinding.FragmentOnboardingBinding
import kg.sunrise.infoapteka.ui.splashScreen.onboarding.onboardingSteps.OnboardingPageFragment
import kg.sunrise.infoapteka.ui.splashScreen.onboarding.onboardingSteps.OnboardingPageDelegate
import kg.sunrise.infoapteka.utils.extensions.navigateToMain

class OnboardingFragment : BaseFragment<FragmentOnboardingBinding>(),
    OnboardingPageDelegate {

    private lateinit var pagerAdapter: ScreenSlidePagerAdapter

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentOnboardingBinding {
        return FragmentOnboardingBinding.inflate(inflater)
    }

    override fun init() {
        setupAdapter()
    }

    private fun setupAdapter() {
        pagerAdapter = ScreenSlidePagerAdapter(childFragmentManager, this)
        binding.liquidPager.adapter = pagerAdapter
        binding.dotsIndicator.setViewPager(binding.liquidPager)
    }

    private class ScreenSlidePagerAdapter(fm: FragmentManager, val delegate: OnboardingPageDelegate)
        : FragmentStatePagerAdapter(fm) {

        override fun getCount(): Int {
            return NUM_PAGES
        }

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> OnboardingPageFragment(R.string.onboarding_text_first, R.drawable.ic_onboarding_first, R.color.white, R.color.secondary_gray, delegate)
                1 -> OnboardingPageFragment(R.string.onboarding_text_second, R.drawable.ic_onboarding_second, R.color.blue, R.color.white, delegate)
                2 -> OnboardingPageFragment(R.string.onboarding_text_third, R.drawable.ic_onboarding_third, R.color.green, R.color.white, delegate)
                else -> throw Throwable("1559 error creating page")
            }
        }

    }

    companion object {

        private val NUM_PAGES = 3

    }

    override fun onSkipClicked() {
        navigateToMain()
    }
}

