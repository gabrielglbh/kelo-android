package com.gabr.gabc.kelo.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.gabr.gabc.kelo.R
import com.gabr.gabc.kelo.firebase.UserQueries
import com.gabr.gabc.kelo.main.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Function that manages the states of Firebase Cloud Messaging for push notifications.
 *
 * Firebase notifies the device with the desired Token when the Cloud Function executes when:
 *  - Someone adds a chore
 *  - Someone removes a chore
 *  - Someone completes a chore
 *
 * As the message is received in [onMessageReceived], we build the actual notification to be shown
 * in the system to the user WHEN THE APP IS RUNNING IN FOREGROUND and manage all the necessary
 * intents and contents of the notification itself
 * */
open class CustomFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) { updateTokenToUser(token) }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        try {
            remoteMessage.notification?.let { notification ->
                val notifyIntent = Intent(this, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                val notifyPendingIntent = PendingIntent.getActivity(this, 0, notifyIntent, 0)
                val channelId = getString(R.string.default_notification_channel_id)
                val mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channel = NotificationChannel(channelId,
                        "Main channel for KELO chore updates",
                        NotificationManager.IMPORTANCE_HIGH)
                    mNotificationManager.createNotificationChannel(channel)
                }

                val builder = NotificationCompat.Builder(this, channelId).apply {
                    setContentIntent(notifyPendingIntent)
                    setSmallIcon(R.mipmap.kelo_icon)
                    setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.kelo_icon))
                    priority = NotificationManager.IMPORTANCE_HIGH
                    setChannelId(channelId)
                    setVibrate(longArrayOf(1000, 1000))
                    setContentTitle(notification.title)
                    setContentText(notification.body)
                }
                with(NotificationManagerCompat.from(this)) {
                    notify(666, builder.build())
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDeletedMessages() { Log.e("onDeletedMessages", "DELETE MESSAGES") }

    private fun updateTokenToUser(token: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val user = UserQueries().getUser(SharedPreferences.userId, SharedPreferences.groupId)
            user?.let { u ->
                u.messagingToken = token
                UserQueries().updateUser(u, SharedPreferences.groupId)
            }
        }
    }
}