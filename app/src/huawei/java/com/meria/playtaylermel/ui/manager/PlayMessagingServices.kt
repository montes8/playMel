package com.meria.playtaylermel.ui.manager

import android.R.id.message
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
import com.huawei.hms.push.HmsMessageService
import com.huawei.hms.push.RemoteMessage
import com.huawei.hms.push.SendException
import com.meria.playtaylermel.R
import com.meria.playtaylermel.ui.splash.SplashActivity
import com.meria.playtaylermel.util.CODELABS_ACTION
import com.meria.playtaylermel.util.NOTIFICATION_ID
import com.meria.playtaylermel.util.TAG
import com.meria.playtaylermel.util.VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION


class PlayMessagingServices : HmsMessageService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.i(TAG, "receive token:$token")
        sendTokenToDisplay(token)
    }

    private fun sendTokenToDisplay(token: String) {
        val intent = Intent("com.huawei.push.codelab.ON_NEW_TOKEN")
        intent.putExtra("token", token)
        sendBroadcast(intent)
    }

    override fun onMessageReceived(message: RemoteMessage?) {
        super.onMessageReceived(message)

        if (message?.notification != null) {
            makeStatusNotification(
               message.notification?.body,
               message.notification?.title
            )
        }
        if (message?.data?.isNotEmpty() == true) {
            makeStatusNotification(
                message.notification?.body,
                message.notification?.title
            )
        }
    }

  /*  override fun onMessageReceived(message: RemoteMessage?) {
        if (message?.notification != null) {
            makeStatusNotification(
                message.notification?.body,
                message.notification?.title
            )
        }
        if (message?.data?.isNotEmpty() == true) {
            makeStatusNotification(
                message.notification?.body,
                message.notification?.title
            )
        }







        Log.d(TAG, "onMessageReceived is called")
        if (message == null) {
            Log.d(TAG, "Received message entity is null!")
            return
        }
        Log.d(TAG, """getCollapseKey: ${message.collapseKey}getData: ${message.data}getFrom: ${message.from}getTo: ${message.to}getMessageId: ${message.messageId}
        getOriginalUrgency: ${message.originalUrgency}getUrgency: ${message.urgency}getSendTime: ${message.sentTime}getMessageType: ${message.messageType}getTtl: ${message.ttl}"""
        )


        val notification = message.notification
        if (notification != null) {
            Log.d(TAG, """getImageUrl: ${notification.imageUrl}getTitle: ${notification.title}getTitleLocalizationKey: ${notification.titleLocalizationKey}
            getTitleLocalizationArgs: ${Arrays.toString(notification.titleLocalizationArgs)}getBody: ${notification.body}getBodyLocalizationKey: ${notification.bodyLocalizationKey}
            getBodyLocalizationArgs: ${Arrays.toString(notification.bodyLocalizationArgs)}getIcon: ${notification.icon}getSound: ${notification.sound}
            getTag: ${notification.tag}getColor: ${notification.color}getClickAction: ${notification.clickAction}getChannelId: ${notification.channelId}
            getLink: ${notification.link}getNotifyId: ${notification.notifyId}""")
        }
        val intent = Intent()
        intent.action = CODELABS_ACTION
        intent.putExtra("method", "onMessageReceived")
        intent.putExtra("msg", "onMessageReceived called, message id:" + message.messageId + ", payload data:" + message.data)
        sendBroadcast(intent)
        val judgeWhetherIn10s = false

        // If the messages are not processed in 10 seconds, the app needs to use WorkManager for processing.
        if (judgeWhetherIn10s) {
            startWorkManagerJob(message)
        } else {
            // Process message within 10s
            processWithin10s(message)
        }
    }*/


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

    private fun startWorkManagerJob(message: RemoteMessage) {
        Log.d(TAG, "Start new Job processing.")
    }

    private fun processWithin10s(message: RemoteMessage) {
        Log.d("message", "Processing now.$message")
    }

    override fun onMessageSent(msgId: String) {
        Log.d("onMessageSent",
            "onMessageSent called, Message id:$msgId"
        )
        val intent = Intent()
        intent.action = CODELABS_ACTION
        intent.putExtra("method", "onMessageSent")
        intent.putExtra("msg", "onMessageSent called, Message id:$msgId")
        sendBroadcast(intent)
    }

    override fun onSendError(msgId: String, exception: Exception) {
        Log.d("onSendError",
            "onSendError called, message id:" + msgId + ", ErrCode:"
                    + (exception as SendException).errorCode + ", description:" + exception.message
        )
        val intent = Intent()
        intent.action = CODELABS_ACTION
        intent.putExtra("method", "onSendError")
        intent.putExtra(
            "msg", "onSendError called, message id:" + msgId + ", ErrCode:"
                    + exception.errorCode + ", description:" + exception.message
        )
        sendBroadcast(intent)
    }

    override fun onTokenError(e: Exception?) {
        super.onTokenError(e)
        Log.d("onTokenError",
            "onMessageSent called, Message id:${e?.message}"
        )
    }

}