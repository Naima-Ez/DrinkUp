package com.example.drinkup

import android.app.Application
import com.example.drinkup.database.AppDatabase
import com.example.drinkup.utils.PreferencesManager
import com.example.drinkup.workers.ReminderWorker

class DrinkUpApplication : Application() {

    // 🔹 قاعدة البيانات
    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(this)
    }

    // 🔹 مدير الإعدادات
    val preferencesManager: PreferencesManager by lazy {
        PreferencesManager(this)
    }

    override fun onCreate() {
        super.onCreate()

        // Initialize app
        initializeApp()

        // Setup WorkManager للتذكيرات
        setupWorkManager()
    }

    private fun initializeApp() {
        // هنا يمكن تضيف Crashlytics أو Analytics أو أي إعدادات أخرى
    }

    private fun setupWorkManager() {
        // استرجاع userId من PreferencesManager
        val userId = preferencesManager.currentUserId

        if (userId != -1L && preferencesManager.notificationsEnabled) {
            // جدول تذكيرات الشرب
            ReminderWorker.scheduleReminder(this)
        }
    }

    companion object {
        private const val TAG = "DrinkUpApplication"
    }
}
