package kg.sunrise.infoapteka.ui.views

import android.content.Context
import android.content.res.TypedArray
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import androidx.annotation.DrawableRes
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hbb20.CountryCodePicker
import com.santalu.maskara.Mask
import com.santalu.maskara.MaskChangedListener
import com.santalu.maskara.MaskStyle
import com.santalu.maskara.widget.MaskEditText
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.utils.extensions.visible
import java.io.BufferedReader
import java.io.InputStreamReader

/** required: use input type = phone **/
class PhoneNumberInputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseInputView(context, attrs, defStyleAttr) {

    private lateinit var etInput: MaskEditText

    private lateinit var countryCodePicker: CountryCodePicker

    private var maskChangedListener: MaskChangedListener? = null

    private lateinit var _isPhoneNumberValid: MutableLiveData<Boolean>
    val isPhoneNumberValid: LiveData<Boolean> = _isPhoneNumberValid

    private lateinit var countryCodes: MutableMap<String, String>

    override fun inflateView() {
        LayoutInflater.from(context).inflate(
            R.layout.view_phone_number_input,
            this,
            true
        )

        etInput = findViewById(R.id.et_input)
        countryCodePicker = findViewById(R.id.country_code_picker)
        etInput.inputType = InputType.TYPE_CLASS_NUMBER

        countryCodes = getCountries()

        countryCodePicker.setOnCountryChangeListener {
            setupMask()
        }
        _isPhoneNumberValid = MutableLiveData(false)

        super.inflateView()
    }

    override fun setUpEditText(styledAttrs: TypedArray) {

        etInput.addTextChangedListener {
            maskChangedListener?.let { listener ->
                _isPhoneNumberValid.value = listener.isDone
            }
        }

        setupMask()
    }

    private fun setupMask() {
        val currentMask = countryCodes[countryCodePicker.selectedCountryNameCode + countryCodePicker.selectedCountryCode]

        if (!currentMask.isNullOrEmpty()) {

            val mask = Mask(currentMask, '#', MaskStyle.NORMAL)
            if (maskChangedListener != null)
                etInput.removeTextChangedListener(maskChangedListener)

            maskChangedListener = MaskChangedListener(mask)
            etInput.setText("")
            etInput.hint = currentMask.replace("#", "5")
            etInput.addTextChangedListener(maskChangedListener)
        }
    }

    fun getPhone(): String {
        return "+${countryCodePicker.selectedCountryCode}${etInput.unMasked}"
    }

    fun getPhoneWithMask(): String {
        return "+${countryCodePicker.selectedCountryCode} ${etInput.masked}"
    }

    fun setLayoutEnabled(isEnabled: Boolean) {
        etInput.isFocusable = isEnabled
        countryCodePicker.setCcpClickable(isEnabled)
    }

    private fun getCountries(): MutableMap<String, String> {
        val inputStream =  this.context.assets.open("countries.txt")

        val read = BufferedReader(InputStreamReader(inputStream))
        val codeMap = mutableMapOf<String, String>()

        read.useLines {
            it.forEach { s ->
                if (s.isNotEmpty()) {
                    val arr = s.split(";")
                    val code = arr[1] + arr[0]
                    codeMap[code] = if (arr.size > 3) arr[3].replace("X", "#") else ""
                }
            }
        }

        return codeMap
    }

    fun setFullPhoneNumber(phoneNumberWithCode: String) {
        countryCodePicker.registerCarrierNumberEditText(etInput)
        countryCodePicker.fullNumber = phoneNumberWithCode
    }

    fun setLabel(text: String){
        tvLabel.setText(text)
    }

    fun setRightIcon(@DrawableRes drawable: Int){
        ivRightIcon.visible()
        ivRightIcon.setImageResource(drawable)
    }

    fun addTextChangedListener(onChange: (editText: EditText) -> Unit) {
        etInput.addTextChangedListener {
            onChange(etInput)
        }
    }
}