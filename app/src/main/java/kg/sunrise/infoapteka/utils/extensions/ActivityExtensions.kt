package kg.sunrise.infoapteka.utils.extensions

import android.app.Activity
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.enums.UserType
import kg.sunrise.infoapteka.ui.auth.AuthActivity
import kg.sunrise.infoapteka.ui.main.MainActivity
import kg.sunrise.infoapteka.utils.preference.*

fun Activity.transitionFade() {
    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
}


fun Activity.navigateToMain() {
    val intent = Intent(this, MainActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    startActivity(intent)
    transitionFade()
}

fun Activity.navigateToAuth() {
    val intent = Intent(this, AuthActivity::class.java)
    startActivity(intent)
    transitionFade()
}

fun Activity.signOut() {
    FirebaseAuth.getInstance().signOut()
    deleteToken(this)
    setDeviceToken(this, DEFAULT_TOKEN_VALUE)
    setUserType(this, UserType.UNAUTHORIZED)
    navigateToMain()
}