package kg.sunrise.infoapteka.utils.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.button.MaterialButton
import com.google.android.material.internal.ViewUtils.dpToPx
import com.google.android.material.snackbar.Snackbar
import kg.sunrise.infoapteka.ui.views.TextInputView
import com.google.android.material.textview.MaterialTextView
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.ui.main.MainActivity


fun View.hideKeyboard() {
    val imm = context.getSystemService(
            Activity.INPUT_METHOD_SERVICE
    ) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

infix fun ViewGroup.inflate(@LayoutRes id: Int): View {
    return LayoutInflater.from(context).inflate(id, this, false)
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun Fragment.snackbarPosition(view: View, id: Int, duration: Int = Snackbar.LENGTH_SHORT): Snackbar =
        Snackbar.make(view, id.toString(), duration).apply {
            show()
        }


fun Fragment.snackbar(view: View, @StringRes id: Int, duration: Int = Snackbar.LENGTH_LONG): Snackbar =
        Snackbar.make(requireActivity(), view, getString(id), duration).apply {
            show()
        }


fun Context.snackbar(view: View, message: String, duration: Int = Snackbar.LENGTH_LONG): Snackbar =
        Snackbar.make(this, view, message, duration).apply {
            show()
        }
fun MainActivity.addProductToBasketSnackbar() {
    val snackbar = Snackbar.make(binding.navHostContainer, "", Snackbar.LENGTH_LONG)
    snackbar.view.setBackgroundColor(Color.TRANSPARENT)
    val customSnackbar = layoutInflater.inflate(R.layout.layout_snackbar, null)
    val snackbarLayout: Snackbar.SnackbarLayout = snackbar.view as Snackbar.SnackbarLayout
    snackbarLayout.setPadding(0, 0, 0, 0)
    customSnackbar.findViewById<MaterialButton>(R.id.btn_basket).setOnClickListener {
        redirectToBasket()
    }
    snackbarLayout.addView(customSnackbar, 0)

    snackbar.show()
}

fun ViewPager2.setOverScrollModeNever() {
    (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
}

internal fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)

internal fun TextView.setTextColorRes(@ColorRes color: Int) =
        setTextColor(context.getColorCompat(color))

internal fun Context.getDrawableCompat(@DrawableRes drawable: Int) =
        ContextCompat.getDrawable(this, drawable)

fun TextView.setHtml(html: String) {
    text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
    } else {
        Html.fromHtml(html)
    }
}

fun MaterialTextView.textStyleDivider(srcText: String, start: Int, textColor: Int, textSize: Int) {
    val dividedString = SpannableString(srcText)
    dividedString.setSpan(AbsoluteSizeSpan(textSize, true), start, srcText.count(), 0)
    dividedString.setSpan(ForegroundColorSpan(textColor), start, srcText.count(), 0)
    this.text = dividedString
}





fun setImageViewSource(res: Resources? = null, drawable: Int, theme: Resources.Theme? = null): Drawable? {
    return res?.let { ResourcesCompat.getDrawable(it, drawable, theme) }
}

fun TextInputView.setIfNotNull(value: String?) {
    value?.let {
        setInputText(value)
    }
}