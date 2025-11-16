package com.example.drinkup.workers


import android.content.Context
import androidx.work.*
import com.example.drinkup.utils.PreferencesManager
import com.example.drinkup.utils.NotificationHelper
import java.util.concurrent.TimeUnit

class ReminderWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        // Vérifier si les notifications sont activées
        val prefs = PreferencesManager(applicationContext)
        if (prefs.currentUserId  != -1L) {
            val notificationHelper = NotificationHelper(applicationContext)
            notificationHelper.sendReminderNotification()
        }

        return Result.success()
    }

    companion object {
        private const val WORK_NAME = "drink_reminder_work"

        fun scheduleReminder(context: Context, intervalHours: Long = 2) {
            val constraints = Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .build()

            val reminderRequest = PeriodicWorkRequestBuilder<ReminderWorker>(
                intervalHours, TimeUnit.HOURS
            )
                .setConstraints(constraints)
                .setInitialDelay(intervalHours, TimeUnit.HOURS)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.REPLACE,
                reminderRequest
            )
        }

        fun cancelReminder(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
        }
    }
}
