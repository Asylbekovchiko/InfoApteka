package kg.sunrise.infoapteka.utils.network

import android.view.View
import androidx.fragment.app.Fragment
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.utils.constants.*
import kg.sunrise.infoapteka.utils.extensions.snackbar
import kg.sunrise.infoapteka.utils.extensions.signOut
import org.json.JSONObject

enum class ErrorCode {
    NOINTERNET,
    SERVER_UNRESPONSIBLE
}

fun Fragment.parseErrorCode(view : View,
                            state: State.ErrorState,
                            displayErrorLayout: (Boolean, ErrorCode?) -> Unit,
                            showErrorAsDialog: (String) -> Unit,
                            handleCustomError: (String) -> Unit
) {
    when (state.errorCode) {
        DEFAULT_INTEGER -> requireContext().snackbar(view , getString(R.string.error_network))
        DEFAULT_NO_INTERNER_INTEGER -> {
            if (state.errorCode == DEFAULT_NO_INTERNER_INTEGER) {
                displayErrorLayout(true, ErrorCode.NOINTERNET)
            }
        }
        DEFAULT_ERROR_INT -> showErrorAsDialog(state.message)
        401 -> {
            showErrorAsDialog(getString(R.string.Auth_required))
            requireActivity().signOut()
        }
        in 500..514 -> displayErrorLayout(true, ErrorCode.SERVER_UNRESPONSIBLE)
        404 -> showErrorAsDialog(getString(R.string.error_not_found))
        413 -> showErrorAsDialog(getString(R.string.error_big_file))
        else -> {

            try {
                val jsonObject = JSONObject(state.message)

                when {
                    jsonObject.has(DETAIL) -> try {
                        val message = jsonObject.getString(DETAIL)
                        showErrorAsDialog(message)
                    } catch (e: Exception) {
                        val jsonArray = jsonObject.getJSONArray(DETAIL)
                        if (jsonArray.length() > 0) {
                            showErrorAsDialog(jsonArray.getString(1))
                        }
                    }
                    jsonObject.has(MESSAGE) -> try {
                        val message = jsonObject.getString(MESSAGE)
                        showErrorAsDialog(message)
                    } catch (e: Exception) {
                        val jsonArray = jsonObject.getJSONArray(MESSAGE)
                        if (jsonArray.length() > 0) {
                            showErrorAsDialog(jsonArray.getString(1))
                        }
                    }
                    else -> handleCustomError(state.message)
                }
            } catch (e: Exception) {
                handleCustomError(state.message)
            }
        }
    }
}