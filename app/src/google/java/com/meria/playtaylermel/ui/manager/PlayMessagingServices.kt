package com.meria.playtaylermel.ui.manager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.meria.playtaylermel.R
import com.meria.playtaylermel.ui.splash.SplashActivity
import com.meria.playtaylermel.ui.utils.NOTIFICATION_ID
import com.meria.playtaylermel.ui.utils.VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION
import org.koin.core.KoinComponent


class PlayMessagingServices : FirebaseMessagingService(), KoinComponent {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if (remoteMessage.notification != null) {
            makeStatusNotification(
                remoteMessage.notification?.body,
                remoteMessage.notification?.title
            )
        }
        if (remoteMessage.data.isNotEmpty()) {
            makeStatusNotification(
                remoteMessage.notification?.body,
                remoteMessage.notification?.title
            )
        }


    }

    private fun makeStatusNotification(message: String?, title: String?) {
        val intent = Intent(this, SplashActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = resources.getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(applicationContext.resources.getString(R.string.default_notification_channel_id), name, importance)
            channel.description = VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION
            val notificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, applicationContext.resources.getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent)
        NotificationManagerCompat.from(applicationContext)
            .notify(NOTIFICATION_ID, builder.build())
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("notificationToken", token)
    }
}