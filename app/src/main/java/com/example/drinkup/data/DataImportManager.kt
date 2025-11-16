package com.example.drinkup.data

import android.content.Context
import android.net.Uri
import com.example.drinkup.database.entities.DrinkEntry
import com.example.drinkup.database.entities.GoalHistory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class DataImportManager(private val context: Context) {

    data class ImportResult(
        val success: Boolean,
        val drinkEntries: List<DrinkEntry> = emptyList(),
        val goalHistory: List<GoalHistory> = emptyList(),
        val error: String? = null
    )

    suspend fun importFromJSON(uri: Uri, userId: Long): ImportResult = withContext(Dispatchers.IO) {
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val content = inputStream?.bufferedReader()?.use { it.readText() }

            if (content == null) {
                return@withContext ImportResult(false, error = "Impossible de lire le fichier")
            }

            val jsonObject = JSONObject(content)

            // Parse drink entries
            val drinkEntries = mutableListOf<DrinkEntry>()
            val drinkArray = jsonObject.optJSONArray("drinkEntries")

            if (drinkArray != null) {
                for (i in 0 until drinkArray.length()) {
                    val drinkJson = drinkArray.getJSONObject(i)
                    drinkEntries.add(
                        DrinkEntry(
                            userId = userId,
                            date = drinkJson.getString("date"),
                            quantiteMl = drinkJson.getInt("quantiteMl"),
                            timestamp = drinkJson.optLong("timestamp", System.currentTimeMillis())
                        )
                    )
                }
            }

            // Parse goal history
            val goalHistory = mutableListOf<GoalHistory>()
            val goalArray = jsonObject.optJSONArray("goalHistory")

            if (goalArray != null) {
                for (i in 0 until goalArray.length()) {
                    val goalJson = goalArray.getJSONObject(i)
                    goalHistory.add(
                        GoalHistory(
                            userId = userId,
                            objectif = goalJson.getInt("goalMl"),
                            startDate = goalJson.getString("startDate"),
                            endDate = goalJson.optString("endDate", null),
                            isActive = goalJson.optBoolean("isActive", true)
                        )
                    )
                }
            }

            ImportResult(
                success = true,
                drinkEntries = drinkEntries,
                goalHistory = goalHistory
            )
        } catch (e: Exception) {
            ImportResult(false, error = e.message ?: "Erreur d'importation")
        }
    }
}
