package kg.sunrise.infoapteka.utils.extensions

import android.content.Intent
import android.os.Build
import android.widget.Button
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.button.MaterialButton
import kg.sunrise.infoapteka.ui.main.MainActivity
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.base.activity.BaseActivity
import kg.sunrise.infoapteka.ui.auth.AuthActivity


/**
 * Set status bar color only if Build Version >= 23
 */
fun Fragment.setStatusBarColor(@ColorRes color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        requireActivity().window.statusBarColor = requireContext().getColorCompat(color)
    }
}

fun Fragment.navigateToMain() {
    val intent = Intent(requireContext(), MainActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    startActivity(intent)
    requireActivity().transitionFade()
}

fun Fragment.navigateToAuth() {
    val intent = Intent(requireContext(), AuthActivity::class.java)
    startActivity(intent)
    requireActivity().transitionFade()
}

fun FragmentActivity.transitionFade() {
    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
}

fun Fragment.setNoInternetLayoutVisibility(isHasConnection: Boolean) {
    (requireActivity() as? BaseActivity<*>)?.setInternetConnectionVisibility(isHasConnection)
}
fun Fragment.setServiceUnavailableVisibility(serviceAvailable: Boolean) {
    (requireActivity() as? BaseActivity<*>)?.setServiceUnavailableVisibility(serviceAvailable)
}

fun Fragment.bindRefreshBtn(btnClick: () -> Unit) {
    val refreshBtn =
        (requireActivity() as? BaseActivity<*>)?.findViewById<MaterialButton>(R.id.btn_refresh_connection)
    refreshBtn?.setOnClickListener { btnClick.invoke() }
}


