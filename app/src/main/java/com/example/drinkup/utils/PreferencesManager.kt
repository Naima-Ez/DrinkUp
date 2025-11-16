package com.example.drinkup.utils

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // 🔹 هل التطبيق أول مرة يتم تشغيله؟
    var isFirstRun: Boolean
        get() = prefs.getBoolean(KEY_FIRST_RUN, true)
        set(value) = prefs.edit().putBoolean(KEY_FIRST_RUN, value).apply()

    // 🔹 المعرف الحالي للمستخدم
    var currentUserId: Long
        get() = prefs.getLong(KEY_USER_ID, -1)
        set(value) = prefs.edit().putLong(KEY_USER_ID, value).apply()

    // 🔹 هل الإشعارات مفعلة؟
    var notificationsEnabled: Boolean
        get() = prefs.getBoolean(KEY_NOTIFICATIONS, true)
        set(value) = prefs.edit().putBoolean(KEY_NOTIFICATIONS, value).apply()

    // 🔹 اللغة الحالية للتطبيق
    var language: String
        get() = prefs.getString(KEY_LANGUAGE, "fr") ?: "fr"
        set(value) = prefs.edit().putString(KEY_LANGUAGE, value).apply()

    // 🔹 فترة التذكير (ساعات)
    var reminderInterval: Int
        get() = prefs.getInt(KEY_REMINDER_INTERVAL, 2)
        set(value) = prefs.edit().putInt(KEY_REMINDER_INTERVAL, value).apply()

    // 🔹 آخر تاريخ للنسخة الاحتياطية
    var lastBackupTimestamp: Long
        get() = prefs.getLong(KEY_LAST_BACKUP, 0)
        set(value) = prefs.edit().putLong(KEY_LAST_BACKUP, value).apply()

    // 🔹 مسح بيانات المستخدم
    fun clearUserData() {
        prefs.edit().remove(KEY_USER_ID).apply()
    }

    companion object {
        private const val PREFS_NAME = "drinkup_prefs"
        private const val KEY_FIRST_RUN = "is_first_run"
        private const val KEY_USER_ID = "current_user_id"
        private const val KEY_NOTIFICATIONS = "notifications_enabled"
        private const val KEY_LANGUAGE = "language"
        private const val KEY_REMINDER_INTERVAL = "reminder_interval"
        private const val KEY_LAST_BACKUP = "last_backup_timestamp"
    }
}
