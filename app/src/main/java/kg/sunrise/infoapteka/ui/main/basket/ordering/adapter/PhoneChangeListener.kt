package kg.sunrise.infoapteka.ui.main.basket.ordering.adapter

interface PhoneChangeListener {
    fun onPhoneTextChangeListener(text: String, position: Int, isValid: Boolean)
    fun removePhoneAt(position: Int)
    fun addPhone()
}