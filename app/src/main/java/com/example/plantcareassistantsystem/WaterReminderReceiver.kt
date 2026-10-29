package com.example.plantcareassistantsystem

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

class WaterReminderReceiver : BroadcastReceiver() {

    companion object {

        private const val CHANNEL_ID =
            "water_reminder_channel"

        private const val NOTIFICATION_ID = 1001

        fun scheduleReminder(context: Context) {

            val database =
                WaterReminderDatabaseHelper(context)

            val reminder =
                database.getReminder()

            if (reminder == null || !reminder.enabled) {
                database.close()
                return
            }

            val alarmManager =
                context.getSystemService(
                    Context.ALARM_SERVICE
                ) as AlarmManager

            val intent =
                Intent(
                    context,
                    WaterReminderReceiver::class.java
                )

            val pendingIntent =
                PendingIntent.getBroadcast(
                    context,
                    NOTIFICATION_ID,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or
                            PendingIntent.FLAG_IMMUTABLE
                )

            val calendar =
                java.util.Calendar.getInstance().apply {

                    set(
                        java.util.Calendar.HOUR_OF_DAY,
                        reminder.hour
                    )

                    set(
                        java.util.Calendar.MINUTE,
                        reminder.minute
                    )

                    set(
                        java.util.Calendar.SECOND,
                        0
                    )

                    set(
                        java.util.Calendar.MILLISECOND,
                        0
                    )

                    if (
                        timeInMillis <=
                        System.currentTimeMillis()
                    ) {
                        add(
                            java.util.Calendar.DAY_OF_YEAR,
                            reminder.frequencyDays
                        )
                    }
                }

            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )

            database.close()
        }
    }

    override fun onReceive(
        context: Context,
        intent: Intent?
    ) {

        val database =
            WaterReminderDatabaseHelper(context)

        val reminder =
            database.getReminder()

        if (reminder == null || !reminder.enabled) {
            database.close()
            return
        }

        createNotificationChannel(context)

        val notificationIntent =
            Intent(
                context,
                WaterReminderActivity::class.java
            )

        val pendingIntent =
            PendingIntent.getActivity(
                context,
                2001,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or
                        PendingIntent.FLAG_IMMUTABLE
            )

        val notification =
            NotificationCompat.Builder(
                context,
                CHANNEL_ID
            )
                .setSmallIcon(
                    android.R.drawable.ic_popup_reminder
                )
                .setContentTitle(
                    "💧 Time to water your plant"
                )
                .setContentText(
                    "Don't forget to water ${reminder.plantName}."
                )
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(
                            "Your ${reminder.plantName} is due for watering. Open Plant Care Assistant to check your reminder."
                        )
                )
                .setPriority(
                    NotificationCompat.PRIORITY_HIGH
                )
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()

        val notificationManager =
            context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

        notificationManager.notify(
            NOTIFICATION_ID,
            notification
        )

        database.close()

        scheduleReminder(context)
    }

    private fun createNotificationChannel(
        context: Context
    ) {

        if (
            Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.O
        ) {

            val channel =
                NotificationChannel(
                    CHANNEL_ID,
                    "Water Reminders",
                    NotificationManager.IMPORTANCE_HIGH
                )

            channel.description =
                "Reminders for watering plants"

            val manager =
                context.getSystemService(
                    Context.NOTIFICATION_SERVICE
                ) as NotificationManager

            manager.createNotificationChannel(
                channel
            )
        }
    }
}