package com.example.drinkup.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,               // <-- clé primaire auto-générée
    val nom: String,
    val username: String,
    val email: String,
    val password: String,
    val birthDate: String,
    val objectifMl: Int = 2000,
    val notificationsEnabled: Boolean = true,
    val langue: String = "fr",
    val createdAt: Long = System.currentTimeMillis()
)
