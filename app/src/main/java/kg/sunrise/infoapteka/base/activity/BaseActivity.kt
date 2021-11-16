package kg.sunrise.infoapteka.base.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.viewbinding.ViewBinding
import kg.sunrise.infoapteka.utils.extensions.hideKeyboard


abstract class BaseActivity<VB: ViewBinding> : AppCompatActivity(), NavController.OnDestinationChangedListener {

    lateinit var binding: VB
    abstract val bindingInflater: (LayoutInflater) -> VB

    protected abstract val internetConnectionLayout: ConstraintLayout
    protected abstract val serviceUnAvailableLayout: ConstraintLayout

    protected abstract val navContainerId: Int

    override fun onBackPressed() {
        if (internetConnectionLayout.visibility == View.VISIBLE) {
            return
        }

        super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = bindingInflater.invoke(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        val navHostFragment =
            supportFragmentManager.findFragmentById(navContainerId) as? NavHostFragment
        val navController = navHostFragment?.navController
        navController?.addOnDestinationChangedListener(this)
    }

    override fun onStop() {
        super.onStop()
        val navHostFragment =
            supportFragmentManager.findFragmentById(navContainerId) as? NavHostFragment
        val navController = navHostFragment?.navController
        navController?.removeOnDestinationChangedListener(this)
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        currentFocus?.hideKeyboard()
    }

    fun setInternetConnectionVisibility(isHasConnection: Boolean) {
        internetConnectionLayout.visibility = if (isHasConnection) View.GONE else View.VISIBLE
    }

    fun setServiceUnavailableVisibility(isServiceAvailable: Boolean) {
        serviceUnAvailableLayout.visibility = if (isServiceAvailable) View.GONE else View.VISIBLE
    }
}