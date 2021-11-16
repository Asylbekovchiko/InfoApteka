package kg.sunrise.infoapteka.utils

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kg.sunrise.infoapteka.ui.auth.confirm.PhoneConfirmable
import kg.sunrise.infoapteka.utils.constants.SEND_AGAIN_DURATION
import kg.sunrise.infoapteka.utils.extensions.clearPhoneNumber
import java.util.concurrent.TimeUnit

class PhoneAuth(
    private val listener: PhoneConfirmable
) : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

    private var storedVerificationId = ""
    private var token: PhoneAuthProvider.ForceResendingToken? = null

    fun requestConfirmCode(
        activity: Activity,
        phoneNumber: String
    ) {
        val options = PhoneAuthOptions.newBuilder()
            .setPhoneNumber(phoneNumber.clearPhoneNumber())
            .setTimeout(SEND_AGAIN_DURATION, TimeUnit.MILLISECONDS)
            .setActivity(activity)
            .setCallbacks(this)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun checkConfirmCode(confirmCode: String) {
        if (storedVerificationId.isEmpty()) return
        val credential = PhoneAuthProvider.getCredential(
            storedVerificationId, confirmCode
        )
        signInWithPhoneCredential(credential)
    }

    private fun signInWithPhoneCredential(
        credential: PhoneAuthCredential
    ) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    listener.onSigned()
                } else {
                    listener.onVerificationFailed()
                }
            }
    }

    override fun onVerificationCompleted(p0: PhoneAuthCredential) {
        signInWithPhoneCredential(p0)
    }

    override fun onVerificationFailed(p0: FirebaseException) {
        listener.onVerificationFailed()
    }

    override fun onCodeSent(
        p0: String, p1: PhoneAuthProvider.ForceResendingToken
    ) {
        listener.onCodeSent()
        storedVerificationId = p0
        token = p1
    }

}