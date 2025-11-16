package com.example.drinkup.data

import com.example.drinkup.database.AppDatabase
import com.example.drinkup.database.DrinkDao
import com.example.drinkup.database.entities.DrinkEntry
import com.example.drinkup.database.entities.GoalHistory
import com.example.drinkup.database.entities.UserProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

object DatabaseSeeder {

    fun seedDatabase(database: AppDatabase) {
        CoroutineScope(Dispatchers.IO).launch {
            val dao = database.drinkDao()

            // Check if demo user exists
            val existingUser = dao.getUserByEmail("demo@drinkup.com")
            if (existingUser != null) return@launch

            // Create demo user
            val demoUser = UserProfile(
                nom = "naima",
                username = "Demo User",
                email = "demo@drinkup.com",
                password = "demo123",
                birthDate = "01/01/1990"
            )

            // Use registerUser (اسم صحيح من DrinkDao)
            val userId = dao.registerUser(demoUser)

            // Seed drink entries for the last 7 days
            seedDrinkEntries(dao, userId)

            // Seed goal history for the last 14 days
            seedGoalHistory(dao, userId)
        }
    }

    private suspend fun seedDrinkEntries(dao: DrinkDao, userId: Long) {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val calendar = Calendar.getInstance()

        for (dayOffset in 0..6) {
            calendar.time = Date()
            calendar.add(Calendar.DAY_OF_YEAR, -dayOffset)
            val date = dateFormat.format(calendar.time)

            val numberOfDrinks = (3..8).random()
            for (i in 0 until numberOfDrinks) {
                val quantity = listOf(100, 200, 250, 300, 500).random()
                val drink = DrinkEntry(
                    userId = userId,
                    quantiteMl = quantity,
                    date = date,
                    timestamp = calendar.timeInMillis + (i * 3600000)
                )
                dao.insertDrink(drink)
            }
        }
    }

    private suspend fun seedGoalHistory(dao: DrinkDao, userId: Long) {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val calendar = Calendar.getInstance()

        for (dayOffset in 1..14) {
            calendar.time = Date()
            calendar.add(Calendar.DAY_OF_YEAR, -dayOffset)
            val startDate = dateFormat.format(calendar.time)

            val goalHistory = GoalHistory(
                userId = userId,
                objectif = 2500,
                startDate = startDate,
                endDate = null,
                isActive = true
            )

            dao.insertGoalHistory(goalHistory)
        }
    }
}
