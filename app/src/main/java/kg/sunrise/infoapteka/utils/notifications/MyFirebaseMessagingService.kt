package kg.sunrise.infoapteka.utils.notifications

import android.app.NotificationManager
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kg.sunrise.infoapteka.utils.preference.setDeviceToken
import timber.log.Timber

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (!isToShow(remoteMessage.data)) {
            return
        }
        sendNotification(remoteMessage.data)
    }

    private fun isToShow(data: Map<String, String>): Boolean =
        data.containsKey(KEY_TITLE)
                && data.containsKey(KEY_BODY)
                && data.containsKey(KEY_CLICK_ACTION)

    private fun sendNotification(data: Map<String, String>) {
        val notificationManager = ContextCompat.getSystemService(
            applicationContext,
            NotificationManager::class.java
        ) as NotificationManager
        notificationManager.sendNotification(data, applicationContext)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.e("device token: $token")
        setDeviceToken(applicationContext, token)
    }

    companion object {
        const val KEY_TITLE = "title"
        const val KEY_BODY = "body"
        const val KEY_CLICK_ACTION = "click_action"
        const val KEY_ID = "id"
    }
}