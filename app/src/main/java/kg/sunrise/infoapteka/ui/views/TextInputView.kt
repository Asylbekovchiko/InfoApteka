package kg.sunrise.infoapteka.ui.views

import android.content.Context
import android.content.res.TypedArray
import android.text.InputFilter
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.utils.extensions.gone
import kg.sunrise.infoapteka.utils.extensions.hideKeyboard

open class TextInputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseInputView(context, attrs, defStyleAttr) {

    protected lateinit var etInput: EditText
    private lateinit var vFakeClick: View

    fun setLabel(text: String) {
        tvLabel.setText(text)
    }

    fun setInputText(text: String) {
        etInput.setText(text)
    }


    fun getInputText(): String {
        return etInput.text.toString()
    }

    fun setLayoutEnabled(isEnabled: Boolean) {
        vFakeClick.visibility = if (isEnabled) View.GONE else View.VISIBLE
        etInput.isFocusable = isEnabled
    }

    fun setOnLayoutClickListener(clickListener: () -> Unit) {
        setLayoutEnabled(false)
        vFakeClick.setOnClickListener {
            clickListener()
        }
    }

    fun addTextChangedListener(onChange: (editText: EditText) -> Unit) {
        etInput.addTextChangedListener {
            onChange(etInput)
        }
    }

    fun hideKeyboard() {
        etInput.hideKeyboard()
    }

    override fun inflateView() {
        LayoutInflater.from(context).inflate(
            R.layout.view_text_input,
            this,
            true
        )

        etInput = findViewById(R.id.et_input)
        vFakeClick = findViewById(R.id.v_fake_click)

        super.inflateView()
    }

    override fun setUpEditText(styledAttrs: TypedArray) {
        etInput.hint = styledAttrs.getString(R.styleable.TextInputView_hint)

        if (styledAttrs.hasValue(R.styleable.TextInputView_android_inputType)) {
            val inputType = styledAttrs.getInt(
                R.styleable.TextInputView_android_inputType,
                EditorInfo.TYPE_NULL
            )
            etInput.inputType = inputType
        }
    }

    fun setInputLength(length: Int) {
        etInput.filters = arrayOf(InputFilter.LengthFilter(length))
    }

    fun hideDivider(){
        divider.gone()
    }
}