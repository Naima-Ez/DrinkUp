package com.example.drinkup.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goal_history")
data class GoalHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val objectif: Int,
    val isActive: Boolean = true,
    val startDate: String,     // تاريخ بدء الهدف
    val endDate: String? = null // تاريخ نهاية الهدف
)
