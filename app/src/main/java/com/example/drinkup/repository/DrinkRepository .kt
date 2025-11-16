package com.example.drinkup.repository

import androidx.lifecycle.LiveData
import com.example.drinkup.database.DrinkDao
import com.example.drinkup.database.DailySummary
import com.example.drinkup.database.entities.*

class DrinkRepository(private val dao: com.example.drinkup.database.DrinkDao)
 {

    // ---------------------- USER ----------------------
    suspend fun loginUser(email: String, password: String): UserProfile? {
        return dao.getUserByEmailAndPassword(email, password)
    }

    suspend fun registerUser(user: UserProfile): Long {
        return dao.registerUser(user)
    }

    suspend fun updateUser(user: UserProfile) {
        dao.updateUser(user)
    }

    suspend fun getUserById(id: Long): UserProfile? {
        return dao.getUserById(id)
    }

    suspend fun updateNotifications(userId: Long, enabled: Boolean) {
        dao.updateNotifications(userId, enabled)
    }

    suspend fun updateLanguage(userId: Long, language: String) {
        dao.updateLanguage(userId, language)
    }

    suspend fun updateGoal(userId: Long, objectif: Int) {
        dao.updateGoal(userId, objectif)
    }

    // ---------------------- DRINK ----------------------
    suspend fun insertDrink(drink: DrinkEntry) {
        // في DAO لا يوجد دوال مشروبات هنا، لذلك يجب إضافتها إذا أردت
        // مثال:
        // @Insert suspend fun insertDrink(drink: DrinkEntry): Long
        dao.insertDrink(drink)
    }

    suspend fun getTotalByDate(userId: Long, date: String): Int {
        return dao.getTotalByDate(userId, date) ?: 0
    }


    fun getGoalHistoryLive(userId: Long): LiveData<List<GoalHistory>> {
        return dao.getGoalHistoryLive(userId)
    }

    // ---------------------- WEEKLY SUMMARY ----------------------
    suspend fun getWeeklySummary(userId: Long): List<DailySummary> {
        return dao.getWeeklySummary(userId)
    }


}
