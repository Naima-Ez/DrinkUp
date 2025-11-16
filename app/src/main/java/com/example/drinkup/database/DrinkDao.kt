package com.example.drinkup.database

import androidx.room.*
import com.example.drinkup.database.entities.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserProfile): Long

    @Query("SELECT * FROM user_profile WHERE email = :email AND password = :password LIMIT 1")
    suspend fun login(email: String, password: String): UserProfile?

    @Query("SELECT * FROM user_profile WHERE id = :userId")
    fun getUserById(userId: Long): Flow<UserProfile?>

    @Query("SELECT * FROM user_profile WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserProfile?

    @Update
    suspend fun update(user: UserProfile)
}

annotation class DrinkDao
