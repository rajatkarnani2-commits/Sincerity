package com.example.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.data.AppDatabase
import com.example.data.TaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val taskId = intent.getIntExtra("TASK_ID", -1)
        if (taskId == -1) return
        val alarmType = intent.getStringExtra("ALARM_TYPE") ?: "deadline"

        val database = AppDatabase.getDatabase(context)
        val repository = TaskRepository(database.taskDao())

        CoroutineScope(Dispatchers.IO).launch {
            val task = repository.getTaskById(taskId)
            if (task != null && task.status == "Pending") {
                if (alarmType == "deadline") {
                    // Update task status to Missed
                    repository.updateTask(task.copy(status = "Missed"))

                    // Send high priority notification
                    showNotification(
                        context = context,
                        title = "Deadline Missed!",
                        message = "You failed to complete: ${task.title}",
                        channelId = "deadline_missed_channel",
                        channelName = "Deadline Missed Notification"
                    )
                } else {
                    // Pre-deadline reminders
                    val reminderText = when (alarmType) {
                        "15_min" -> "is due in 15 minutes!"
                        "1_hour" -> "is due in 1 hour!"
                        "2_hour" -> "is due in 2 hours!"
                        "3_hour" -> "is due in 3 hours!"
                        else -> "deadline is approaching!"
                    }
                    showNotification(
                        context = context,
                        title = "Upcoming Task Reminder",
                        message = "\"${task.title}\" $reminderText",
                        channelId = "task_reminder_channel",
                        channelName = "Task Reminders"
                    )
                }
            }
        }
    }

    private fun showNotification(
        context: Context,
        title: String,
        message: String,
        channelId: String,
        channelName: String
    ) {
        val notificationId = System.currentTimeMillis().toInt()
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifies for task deadlines and reminders"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val openIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(notificationId, notification)
    }
}
