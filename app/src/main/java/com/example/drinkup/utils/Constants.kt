package com.example.drinkup.utils


object Constants {
    // Database
    const val DATABASE_NAME = "drinkup_database"
    const val DATABASE_VERSION = 1

    // SharedPreferences
    const val PREFS_NAME = "drinkup_prefs"
    const val KEY_FIRST_RUN = "is_first_run"
    const val KEY_USER_ID = "user_id"
    const val KEY_LAST_VOLUME = "last_volume"

    // Default values
    const val DEFAULT_GOAL_ML = 2000
    const val DEFAULT_REMINDER_INTERVAL_HOURS = 2L

    // Notification
    const val NOTIFICATION_CHANNEL_ID = "drink_reminder_channel"
    const val NOTIFICATION_ID = 1001

    // Date formats
    const val DATE_FORMAT = "dd/MM/yyyy"
    const val TIME_FORMAT = "HH:mm"
    const val DATETIME_FORMAT = "dd/MM/yyyy HH:mm"

    // Quick add volumes
    val QUICK_ADD_VOLUMES = listOf(100, 200, 300, 500)

    // Motivation thresholds
    const val THRESHOLD_START = 25
    const val THRESHOLD_GOOD = 50
    const val THRESHOLD_GREAT = 75
    const val THRESHOLD_ACHIEVED = 100
}