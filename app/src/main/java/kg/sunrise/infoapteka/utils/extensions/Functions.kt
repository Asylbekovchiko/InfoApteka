package kg.sunrise.infoapteka.utils.extensions

import android.Manifest
import android.app.DatePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.widget.DatePicker
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.ui.main.menu.profile.adapter.ProfileAdapter
import kg.sunrise.infoapteka.ui.main.menu.profile.adapter.ProfileDateDelegate
import kg.sunrise.infoapteka.utils.network.State
import kg.sunrise.infoapteka.utils.parsers.DateTimeParser
import kg.sunrise.infoapteka.utils.parsers.DateTimePattern
import kg.sunrise.infoapteka.utils.preference.setLocalization
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList
import android.content.pm.ActivityInfo

import android.content.pm.PackageInfo
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import android.view.View
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat.startActivity
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlin.coroutines.suspendCoroutine


fun setLocale(context: Context, lang: String) {
    val locale = Locale(lang)
    Locale.setDefault(locale)
    val resources: Resources = context.resources
    val config: Configuration = resources.configuration
    config.setLocale(locale)
    resources.updateConfiguration(config, resources.displayMetrics)
    setLocalization(context, lang)
}

fun initDatePicker(delegate: ProfileDateDelegate, context: Context) {
    val calendar = Calendar.getInstance()
    var day = calendar.get(Calendar.DAY_OF_MONTH)
    var month = calendar.get(Calendar.MONTH)
    var year = calendar.get(Calendar.YEAR)

    val datePicker = DatePickerDialog(
        context, R.style.DatePicker,
        { view: DatePicker?, _year: Int, _month: Int, _dayOfMonth: Int ->
            day = _dayOfMonth
            month = _month + 1
            year = _year

            val adapter = ProfileAdapter(delegate)
            val updateDate = DateTimeParser.formatDateTime(
                context.getString(R.string.date_creator, day, month, year),
                DateTimePattern.dd_MM_yyyy_with_spacing,
                DateTimePattern.yyyy_MM_dd_with_dash
            )
            adapter.delegate.updateDateClick(updateDate)
        }, year, month, day
    )
    datePicker.setCancelable(false)
    datePicker.datePicker.maxDate = System.currentTimeMillis()
    datePicker.window!!.setBackgroundDrawableResource(R.drawable.shape_ffffff_radius_12dp)
    datePicker.show()
    datePicker.getButton(DatePickerDialog.BUTTON_POSITIVE).setText(R.string.ok)
    datePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE).setText(R.string.abolition)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        datePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE)
            .setTextColor(context.resources.getColor(R.color.secondary_red, null))
        datePicker.getButton(DatePickerDialog.BUTTON_POSITIVE)
            .setTextColor(context.resources.getColor(R.color.green, null))
    } else {
        datePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE)
            .setTextColor(context.resources.getColor(R.color.secondary_red))
        datePicker.getButton(DatePickerDialog.BUTTON_POSITIVE)
            .setTextColor(context.resources.getColor(R.color.green))
    }
}

fun parseUriToFile(uri: Uri): MultipartBody.Part {
    val file = File(uri.path ?: "")
    val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
    val fileBody = MultipartBody.Part.createFormData("avatar", file.name, requestFile)
    return fileBody
}

fun parseListUri(listUri: ArrayList<Uri>): ArrayList<MultipartBody.Part> {
    val arrParsedUris = arrayListOf<MultipartBody.Part>()
    for (i in listUri) {
        val file = File(i.path ?: "")
        val requestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val fileBody = MultipartBody.Part.createFormData("image", file.name, requestBody)
        arrParsedUris.add(fileBody)
    }
    return arrParsedUris
}


fun setImageWithPlaceHolder(
    src: String,
    view: ImageView,
    placeHolder: Int = R.drawable.shape_ffffff_rad_16dp
) {
    Glide.with(view.context).load(src).placeholder(placeHolder).into(view)
}

fun Long.toMb(): Double = (this.toDouble() / 8 / 1024.0 / 1024.0)

fun convertToRoundNumber(num: Double): String {
    val roundedSize = num.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
    val format = DecimalFormat("###.#")
    val finalSize = format.format(roundedSize)
    return finalSize
}


fun Int.toMb(): Double = (this.toDouble() / 1024.0 / 1024.0)


infix fun <T> MutableLiveData<T>.updateValue(arg: T) {
    this.value = arg
}

infix fun <T> MutableLiveData<State>.updateSuccessObjectValue(
    arg: T
) {
    this.value = State.SuccessState.SuccessObjectState(arg)
}

fun bitmapFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
    val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
    vectorDrawable!!.setBounds(
        0,
        0,
        vectorDrawable.intrinsicWidth,
        vectorDrawable.intrinsicHeight
    )
    val bitmap = Bitmap.createBitmap(
        vectorDrawable.intrinsicWidth,
        vectorDrawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )

    val canvas = Canvas(bitmap)
    vectorDrawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

fun hasLocationPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

fun <T> Task<T>.asDeferred(): Deferred<T> {
    val deferred = CompletableDeferred<T>()
    deferred.invokeOnCompletion {
        if (deferred.isCancelled) {
        }
    }
    this.addOnSuccessListener { result -> deferred.complete(result) }
    this.addOnFailureListener { exception -> deferred.completeExceptionally(exception) }

    return deferred
}

fun checkIfGpsEnabled(context: Context): Boolean {
    return (context.getSystemService(Context.LOCATION_SERVICE) as LocationManager).isProviderEnabled(
        LocationManager.GPS_PROVIDER
    )
}

fun turnOnGPS(context: Context) {
    val locationIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    context.startActivity(locationIntent)
}