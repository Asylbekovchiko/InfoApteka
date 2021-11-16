package kg.sunrise.infoapteka.ui.auth

import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.activity.BaseActivity
import kg.sunrise.infoapteka.databinding.ActivityAuthBinding
import kg.sunrise.infoapteka.utils.extensions.transitionFade

class AuthActivity : BaseActivity<ActivityAuthBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityAuthBinding = {
        ActivityAuthBinding.inflate(it)
    }
    override val internetConnectionLayout: ConstraintLayout by lazy {
        binding.inclProgress.clProgress
    }

    override val serviceUnAvailableLayout: ConstraintLayout
        get() = binding.inclServiceUnavailable.clServiceUnavailable

    override val navContainerId: Int = R.id.nav_container

    override fun onBackPressed() {
        val currentID = binding.navContainer.findNavController().currentDestination?.id
        val sourceID = R.id.registrationSuccessfulFragment

        if (currentID == sourceID) {
            return
        }

        super.onBackPressed()
        transitionFade()
    }

}