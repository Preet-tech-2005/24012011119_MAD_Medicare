package com.example.a24012011119_mad_medicare

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {

        val medicineName =
            intent.getStringExtra("medicine_name")
                ?: "Medicine"


        // This Toast confirms that the alarm fired

        Toast.makeText(
            context,
            "Alarm Running: $medicineName",
            Toast.LENGTH_LONG
        ).show()


        val channelId =
            "medicine_reminder"


        val notificationManager =
            context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager


        // Notification Channel for Android 8+

        if (Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.O
        ) {

            val channel =
                NotificationChannel(
                    channelId,
                    "Medicine Reminder",
                    NotificationManager.IMPORTANCE_HIGH
                )

            notificationManager.createNotificationChannel(
                channel
            )
        }


        // Create Notification

        val notification =
            NotificationCompat.Builder(
                context,
                channelId
            )
                .setSmallIcon(
                    android.R.drawable.ic_dialog_info
                )
                .setContentTitle(
                    "Medicine Reminder"
                )
                .setContentText(
                    "Time to take $medicineName"
                )
                .setPriority(
                    NotificationCompat.PRIORITY_HIGH
                )
                .setAutoCancel(true)
                .build()


        // Show Notification

        notificationManager.notify(
            1001,
            notification
        )
    }
}