package kg.sunrise.infoapteka.ui.auth.confirm

interface PhoneConfirmable {
    fun onVerificationFailed()
    fun onCodeSent()
    fun onSigned()
}