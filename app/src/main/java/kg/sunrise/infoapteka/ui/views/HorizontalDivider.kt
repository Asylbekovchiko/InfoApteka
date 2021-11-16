package kg.sunrise.infoapteka.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import kg.sunrise.infoapteka.R

class HorizontalDivider @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var dividerView: View

    init {
        LayoutInflater.from(context).inflate(
            R.layout.view_horizontal_divider,
            this,
            true
        )

        dividerView = findViewById(R.id.divider)
    }

    fun setDividerBackgroundColor(dividerColor: Int) {
        dividerView.setBackgroundResource(dividerColor)
    }
}