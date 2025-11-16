package com.example.drinkup.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "drink_entry")
data class DrinkEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val quantiteMl: Int,
    val date: String,
    val timestamp: Long = System.currentTimeMillis()
)
