package kg.sunrise.infoapteka.ui.splashScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.activity.BaseActivity
import kg.sunrise.infoapteka.databinding.ActivityPrefaceBinding

class PrefaceActivity : BaseActivity<ActivityPrefaceBinding>() {

    override val navContainerId: Int = R.id.nav_container
    override val internetConnectionLayout: ConstraintLayout by lazy {
        binding.inclNoInternet.clNoInternet
    }

    override val serviceUnAvailableLayout: ConstraintLayout
        get() = binding.inclServiceUnavailable.clServiceUnavailable

    override val bindingInflater: (LayoutInflater) -> ActivityPrefaceBinding = { it ->
        ActivityPrefaceBinding.inflate(it)
    }
}