package kg.sunrise.infoapteka.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.textview.MaterialTextView
import kg.sunrise.infoapteka.R

class LoadFileView @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet,
    defStyleAttr: Int = 0
) :
    ConstraintLayout(context, attributes, defStyleAttr) {

    protected lateinit var tvPressUpload: ConstraintLayout
    private val title: MaterialTextView by lazy {
        findViewById(R.id.tv_title)
    }

    init {
        inflateView()
    }

    fun setClickListenerOnUpload(click: () -> Unit) {
        tvPressUpload.setOnClickListener {
            click()
        }
    }

    private fun inflateView() {
        LayoutInflater.from(context).inflate(R.layout.view_load_file, this, true)
        tvPressUpload = findViewById(R.id.cl_certificate_load)
    }

    fun setTitle(text: String) {
        title.text = text
    }
}