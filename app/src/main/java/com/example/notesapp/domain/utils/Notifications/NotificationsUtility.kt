package com.example.notesapp.domain.utils.Notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.notesapp.R

// object using OS to create a channel and send notification
object NotificationsUtility{
    // creating a channel for my notifications to group them together and make them managable by managing a group
    private const val CHANNEL_ID = "reminder_channel" // unique identifier of the created channel for the app

    // create notification channel
    // from android 8.0+ notifications must belong to a channel. Without this app can't show notifications
    fun createNotificationChannel(context: Context){
        val name = "Reminders" // this shows up in app permissions manager permissions -> notifications -> Reminders(able to switch off)
        val descriptionText = "Reminder notifications"
        val importance = NotificationManager.IMPORTANCE_DEFAULT // determines how intrusive the notification is

        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(channel)
    }

    // uses android OS to display a notification
    fun showNotification(context: Context, title: String, message: String){
        // check if permissions needed (android 13+ needs explicit permissions to show the notification, if not granted - return )
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
        }

        // Set the notification details that show up
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_reminder)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_reminder))
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)


        // finally show the notification
        NotificationManagerCompat.from(context).notify(1,builder.build())



    }


}