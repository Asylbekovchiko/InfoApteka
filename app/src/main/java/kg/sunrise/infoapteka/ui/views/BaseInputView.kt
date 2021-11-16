package kg.sunrise.infoapteka.ui.views

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.constraintlayout.widget.ConstraintLayout
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.utils.extensions.getColorCompat
import kg.sunrise.infoapteka.utils.extensions.gone
import kg.sunrise.infoapteka.utils.extensions.visible

abstract class BaseInputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    protected lateinit var clLabelContainer: ConstraintLayout
    protected lateinit var tvLabel: TextView
    protected lateinit var tvStar: TextView
    protected lateinit var tvError: TextView
    protected lateinit var ivLeftIcon: ImageView
    protected lateinit var ivRightIcon: ImageView
    protected lateinit var divider: View

    protected val defaultErrorMessage: String

    init {
        inflateView()

        val styledAttrs =
            context.obtainStyledAttributes(attrs, R.styleable.TextInputView)

        if (styledAttrs.hasValue(R.styleable.TextInputView_label)) {
            clLabelContainer.visible()

            tvLabel.text = styledAttrs.getString(R.styleable.TextInputView_label)

            tvStar.visibility = if (styledAttrs.getBoolean(
                    R.styleable.TextInputView_isRequired,
                    false
                )
            ) View.VISIBLE else View.GONE
        }

        defaultErrorMessage = styledAttrs.getString(R.styleable.TextInputView_errorText) ?: ""
        tvError.text = defaultErrorMessage

        setUpEditText(styledAttrs)

        if (styledAttrs.hasValue(R.styleable.TextInputView_editTextRightIcon)) {
            ivRightIcon.setImageDrawable(styledAttrs.getDrawable(R.styleable.TextInputView_editTextRightIcon))
            ivRightIcon.visible()
        }

        if (styledAttrs.hasValue(R.styleable.TextInputView_editTextLeftIcon)) {
            ivLeftIcon.setImageDrawable(styledAttrs.getDrawable(R.styleable.TextInputView_editTextLeftIcon))
            ivLeftIcon.visible()
        }

        styledAttrs.recycle()
    }

    protected abstract fun setUpEditText(styledAttrs: TypedArray)
    protected open fun inflateView() {
        clLabelContainer = findViewById(R.id.cl_label_container)
        tvLabel = findViewById(R.id.tv_label)
        tvStar = findViewById(R.id.tv_star)
        tvError = findViewById(R.id.tv_error)
        ivLeftIcon = findViewById(R.id.iv_left_icon)
        ivRightIcon = findViewById(R.id.iv_right_icon)
        divider = findViewById(R.id.divider)
    }

    fun setOnLeftIconClickListener(onClick: () -> Unit) {
        ivLeftIcon.setOnClickListener {
            onClick()
        }
    }

    fun setOnRightIconClickListener(onClick: () -> Unit) {
        ivRightIcon.setOnClickListener {
            onClick()
        }
    }

    fun showError(message: String? = null) {
        val errorMessage = message ?: defaultErrorMessage
        tvError.text = errorMessage
        setErrorViewsColor()
    }

    fun hideError() {
        setDefaultViewsColor()
    }

    fun setRequire(require : Boolean) {
        if(require)
            tvStar.visible()
        else
            tvStar.gone()

    }

    private fun setDefaultViewsColor() {
        setViewsColor(R.color.black)
        divider.setBackgroundResource(R.color.light)
        tvError.gone()
    }

    private fun setErrorViewsColor() {
        setViewsColor(R.color.secondary_red)
        divider.setBackgroundResource(R.color.secondary_red)
        tvError.visible()
    }

    private fun setViewsColor(@ColorRes colorRes: Int) {
        tvLabel.setTextColor(context.getColorCompat(colorRes))
        tvStar.setTextColor(context.getColorCompat(colorRes))
    }
}