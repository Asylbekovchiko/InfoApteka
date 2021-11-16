package kg.sunrise.infoapteka.utils.extensions

import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton

fun MaterialButton.setEnabled(@ColorRes color: Int) {
    backgroundTintList =
        ContextCompat.getColorStateList(context, color)
    isEnabled = true
}

fun MaterialButton.setDisabled(@ColorRes color: Int) {
    backgroundTintList =
        ContextCompat.getColorStateList(context, color)
    isEnabled = false
}