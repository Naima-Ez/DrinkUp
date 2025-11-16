package com.example.drinkup.dao

import androidx.room.*
import com.example.drinkup.database.entities.GoalHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalHistoryDao {
    @Insert
    suspend fun insert(goal: GoalHistory): Long

    @Query("SELECT * FROM goal_history WHERE userId = :userId AND isActive = 1 LIMIT 1")
    fun getActiveGoal(userId: Long): Flow<GoalHistory?>

    @Query("SELECT * FROM goal_history WHERE userId = :userId ORDER BY startDate DESC")
    fun getAllGoals(userId: Long): Flow<List<GoalHistory>>

    @Query("UPDATE goal_history SET isActive = 0, endDate = :endDate WHERE id = :goalId")
    suspend fun deactivateGoal(goalId: Long, endDate: String)

    @Update
    suspend fun update(goal: GoalHistory)
}