package com.example.drinkup.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.drinkup.database.entities.DrinkEntry
import com.example.drinkup.database.entities.UserProfile
import com.example.drinkup.database.DailySummary
import com.example.drinkup.database.entities.GoalHistory


@Dao
interface DrinkDao {

    // ===================== USER PROFILE =====================
    @Insert
    suspend fun insertUser(user: UserProfile): Long = registerUser(user)

    @Insert
    suspend fun registerUser(user: UserProfile): Long

    @Update
    suspend fun updateUser(user: UserProfile)

    @Query("SELECT * FROM user_profile WHERE id = :userId LIMIT 1")
    suspend fun getUserById(userId: Long): UserProfile?

    @Query("SELECT * FROM user_profile WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserProfile?

    @Query("SELECT * FROM user_profile WHERE email = :email AND password = :password LIMIT 1")
    suspend fun getUserByEmailAndPassword(email: String, password: String): UserProfile?

    @Query("UPDATE user_profile SET notificationsEnabled = :enabled WHERE id = :userId")
    suspend fun updateNotifications(userId: Long, enabled: Boolean)

    @Query("UPDATE user_profile SET langue = :langue WHERE id = :userId")
    suspend fun updateLanguage(userId: Long, langue: String)

    @Query("UPDATE user_profile SET objectifMl = :objectif WHERE id = :userId")
    suspend fun updateGoal(userId: Long, objectif: Int)

    // ===================== DRINK ENTRY =====================
    @Insert
    suspend fun insertDrink(drink: DrinkEntry): Long

    @Query("SELECT * FROM drink_entry WHERE userId = :userId AND date = :date ORDER BY timestamp DESC")
    suspend fun getDrinksByDate(userId: Long, date: String): List<DrinkEntry>

    @Query("SELECT * FROM drink_entry WHERE userId = :userId AND date = :date ORDER BY timestamp DESC")
    fun getDrinksByDateLive(userId: Long, date: String): LiveData<List<DrinkEntry>>

    @Query("""
        SELECT date, SUM(quantiteMl) AS total
        FROM drink_entry
        WHERE userId = :userId
        GROUP BY date
        ORDER BY date DESC
        LIMIT 7
    """)
    suspend fun getWeeklySummary(userId: Long): List<DailySummary>

    @Query("SELECT SUM(quantiteMl) FROM drink_entry WHERE userId = :userId AND date = :date")
    suspend fun getTotalByDate(userId: Long, date: String): Int?

    @Query("SELECT SUM(quantiteMl) FROM drink_entry WHERE userId = :userId AND date = :date")
    fun getTotalByDateLive(userId: Long, date: String): LiveData<Int?>



    // ===================== GOAL HISTORY =====================
    // ===================== GOAL HISTORY =====================
    @Insert
    suspend fun insertGoalHistory(goalHistory: GoalHistory): Long

    @Query("SELECT * FROM goal_history WHERE userId = :userId ORDER BY startDate DESC")
    fun getGoalHistoryLive(userId: Long): LiveData<List<GoalHistory>>

    // دالة جديدة لـ BackupManager
    @Query("SELECT * FROM goal_history WHERE userId = :userId ORDER BY startDate DESC")
    suspend fun getGoalHistory(userId: Long): List<GoalHistory>

}


