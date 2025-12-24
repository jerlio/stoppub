package com.jelio.stoppub

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat

object NotificationHelper {

    private const val CHANNEL_ID = "blocked_calls_channel"
    private const val CHANNEL_NAME = "Appels bloqués"
    private const val NOTIFICATION_ID = 1

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_LOW // Silent notification
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = "Notifications pour les appels bloqués"
                setSound(null, null) // No sound for silent notification
                enableVibration(false)
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showBlockedCallNotification(context: Context, phoneNumber: String?) {
        // Check if notifications are enabled in settings
        if (!SettingsStore.isNotificationsEnabled(context)) {
            return
        }

        createNotificationChannel(context)

        val displayNumber = phoneNumber ?: "Numéro inconnu"
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_call) // Using system icon
            .setContentTitle("Appel bloqué")
            .setContentText("Numéro bloqué : $displayNumber")
            .setPriority(NotificationCompat.PRIORITY_LOW) // Low priority for silent notification
            .setAutoCancel(true)
            .setSilent(true) // Ensure it's silent
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}
