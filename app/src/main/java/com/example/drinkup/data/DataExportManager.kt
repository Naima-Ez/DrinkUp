package com.example.drinkup.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.drinkup.database.entities.DrinkEntry
import com.example.drinkup.database.entities.GoalHistory
import com.example.drinkup.database.entities.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

class DataExportManager(private val context: Context) {

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())

    // Export to CSV
    suspend fun exportToCSV(
        user: UserProfile,
        drinkEntries: List<DrinkEntry>,
        goalHistory: List<GoalHistory>
    ): Uri? = withContext(Dispatchers.IO) {
        try {
            val fileName = "drinkup_export_${System.currentTimeMillis()}.csv"
            val file = File(context.getExternalFilesDir(null), fileName)

            FileWriter(file).use { writer ->
                // Header
                writer.append("Type,Date,Quantité (ml),Objectif (ml),Atteint\n")

                // Drink entries
                drinkEntries.forEach { drink ->
                    writer.append("Consommation,${drink.date},${drink.quantiteMl},,\n")
                }

                // Goal history
                goalHistory.forEach { goal ->
                    writer.append(
                        "Objectif,${goal.startDate},,${goal.goalMl},${if (goal.isActive) "Oui" else "Non"}\n"
                    )
                }
            }

            Uri.fromFile(file)
        } catch (e: Exception) {
            null
        }
    }

    // Export to JSON
    suspend fun exportToJSON(
        user: UserProfile,
        drinkEntries: List<DrinkEntry>,
        goalHistory: List<GoalHistory>
    ): Uri? = withContext(Dispatchers.IO) {
        try {
            val fileName = "drinkup_backup_${System.currentTimeMillis()}.json"
            val file = File(context.getExternalFilesDir(null), fileName)

            val jsonObject = JSONObject().apply {
                put("exportDate", dateFormat.format(Date()))
                put("version", "1.0")

                // User data
                put("user", JSONObject().apply {
                    put("username", user.username)
                    put("email", user.email)
                    put("birthDate", user.birthDate)
                })

                // Drink entries
                put("drinkEntries", JSONArray().apply {
                    drinkEntries.forEach { drink ->
                        put(JSONObject().apply {
                            put("date", drink.date)
                            put("quantiteMl", drink.quantiteMl)
                            put("timestamp", drink.timestamp)
                        })
                    }
                })

                // Goal history
                put("goalHistory", JSONArray().apply {
                    goalHistory.forEach { goal ->
                        put(JSONObject().apply {
                            put("goalMl", goal.goalMl)
                            put("startDate", goal.startDate)
                            put("endDate", goal.endDate)
                            put("isActive", goal.isActive)
                        })
                    }
                })
            }

            FileWriter(file).use { writer ->
                writer.write(jsonObject.toString(4))
            }

            Uri.fromFile(file)
        } catch (e: Exception) {
            null
        }
    }

    // Share exported file
    fun shareFile(uri: Uri, mimeType: String = "text/csv") {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = mimeType
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(
            Intent.createChooser(intent, "Exporter les données")
        )
    }
}
