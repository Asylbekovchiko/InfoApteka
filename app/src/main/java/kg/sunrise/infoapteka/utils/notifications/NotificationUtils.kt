package kg.sunrise.infoapteka.utils.notifications

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.NavigationRes
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import kg.sunrise.infoapteka.R
import kg.sunrise.infoapteka.ui.main.MainActivity

@SuppressLint("UnspecifiedImmutableFlag")
fun NotificationManager.sendNotification(
    data: Map<String, String>,
    context: Context
) {
    val messageTitle =
        data[MyFirebaseMessagingService.KEY_TITLE]
    val messageBody =
        data[MyFirebaseMessagingService.KEY_BODY]

    val soundUri =
        RingtoneManager.getDefaultUri(
            RingtoneManager.TYPE_NOTIFICATION
        )

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        createNotificationChannel(context)?.let {
            createNotificationChannel(it)
        }
    }

    val pendingIntent = buildPendingIntent(data, context)

    val builder = NotificationCompat.Builder(
        context,
        context.getString(
            R.string.default_notification_channel_id
        )
    )
        .setSmallIcon(R.drawable.ic_logo)
        .setContentTitle(messageTitle)
        .setSound(soundUri)
        .setContentText(messageBody)
        .setContentIntent(pendingIntent)
        .setPriority(NotificationCompat.PRIORITY_MAX)
        .setAutoCancel(true)

    notify(NOTIFICATION_ID, builder.build())
}

private fun createNotificationChannel(
    context: Context
): NotificationChannel? {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channelId =
            context.getString(
                R.string.default_notification_channel_id
            )
        val name = context.getString(R.string.Notifications)
        val descriptionText = context.getString(
            R.string.default_notification_channel_id
        )
        val channelImportance =
            NotificationManager.IMPORTANCE_HIGH

        return NotificationChannel(
            channelId, name, channelImportance
        ).apply {
            description = descriptionText
        }
    }

    return null
}

private fun buildPendingIntent(data: Map<String, String>, context: Context): PendingIntent {
    @NavigationRes
    var graph: Int = 0
    @IdRes
    var destination: Int = 0
    var bundle: Bundle = bundleOf()


    when (data["type"]?.uppercase()) {
        NotificationType.ACCOUNT_APPROVED.name, NotificationType.ACCOUNT_REJECTED.name -> {
            graph = R.navigation.menu_nav_graph
            destination = R.id.menuFragment
        }
        NotificationType.DRUG_APPROVED.name, NotificationType.DRUG_REJECTED.name -> {
            graph = R.navigation.home_nav_graph
            destination = R.id.productDetailFragment
            bundle = bundleOf(
                "productId" to (data["drug_id"] ?: error("-1")).toInt()
            )
        }
        NotificationType.ORDER_DELIVERED.name, NotificationType.ORDER_PROCESSED.name -> {
            graph = R.navigation.menu_nav_graph
            destination = R.id.ordersHistoryFragment
        }
        else -> {
            graph = R.navigation.home_nav_graph
            destination = R.id.homeFragment
        }
    }

    return NavDeepLinkBuilder(context)
        .setComponentName(MainActivity::class.java)
        .setGraph(graph)
        .setDestination(destination)
        .setArguments(bundle)
        .createPendingIntent()
}

enum class NotificationType {
    ACCOUNT_APPROVED,
    ACCOUNT_REJECTED,
    DRUG_APPROVED,
    DRUG_REJECTED,
    ORDER_PROCESSED,
    ORDER_DELIVERED
}

const val NOTIFICATION_ID = 0