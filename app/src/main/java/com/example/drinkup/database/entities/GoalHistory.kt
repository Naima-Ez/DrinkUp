package com.example.drinkup.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goal_history")
data class GoalHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val goalMl: Int,
    val startDate: String, // yyyy-MM-dd
    val endDate: String?, // null if current goal
    val isActive: Boolean = true
)