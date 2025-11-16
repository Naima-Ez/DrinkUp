package com.example.drinkup.repository

import androidx.lifecycle.LiveData
import androidx.room.Query
import com.example.drinkup.database.DrinkDao
import com.example.drinkup.database.DailySummary

import com.example.drinkup.database.entities.*



class DrinkRepository(private val dao: DrinkDao) {

    // ---------------------- USER ----------------------



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

    // ---------------------- DRINK ----------------------

    suspend fun insertDrink(drink: DrinkEntry) {
        dao.insertDrink(drink)
    }

    suspend fun getTotalByDate(userId: Long, date: String): Int {
        return dao.getTotalByDate(userId, date)
    }

    // ---------------------- GOAL HISTORY ----------------------

    fun getGoalHistoryLive(userId: Long): LiveData<List<GoalHistory>> {
        return dao.getGoalHistoryLive(userId)
    }

    // ---------------------- WEEKLY SUMMARY ----------------------

    suspend fun getWeeklySummary(userId: Long): List<DailySummary> {
        return dao.getWeeklySummary(userId)
    }
}



