package com.example.drinkup.data

import android.content.Context
import android.net.Uri
import com.example.drinkup.database.AppDatabase
import com.example.drinkup.utils.PreferencesManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class BackupManager(private val context: Context) {

    private val prefsManager = PreferencesManager(context)
    private val exportManager = DataExportManager(context)
    private val database = AppDatabase.getDatabase(context)

    companion object {
        private const val BACKUP_DIR = "backups"
        private const val MAX_BACKUPS = 5
        private const val BACKUP_INTERVAL_DAYS = 7
    }

    // Check if backup is needed
    fun shouldCreateBackup(): Boolean {
        val lastBackup = prefsManager.lastBackupTimestamp
        val now = System.currentTimeMillis()
        val daysSinceBackup = (now - lastBackup) / (1000 * 60 * 60 * 24)

        return daysSinceBackup >= BACKUP_INTERVAL_DAYS
    }

    // Create automatic backup
    fun createAutomaticBackup() {
        if (!shouldCreateBackup()) return

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userId = prefsManager.currentUserId
                if (userId == -1L) return@launch

                val dao = database.drinkDao()
                val user = dao.getUserById(userId) ?: return@launch

                // Get all data
                val drinkEntries = dao.getDrinksByDate(userId, "")
                val goalHistory = dao.getGoalHistory(userId)

                // Create backup file
                val backupUri = exportManager.exportToJSON(user, drinkEntries, goalHistory)

                if (backupUri != null) {
                    // Move to backup directory
                    moveToBackupDir(backupUri)

                    // Clean old backups
                    cleanOldBackups()

                    // Update last backup time
                    prefsManager.lastBackupTimestamp = System.currentTimeMillis()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun moveToBackupDir(sourceUri: Uri) {
        val backupDir = File(context.getExternalFilesDir(null), BACKUP_DIR)
        if (!backupDir.exists()) {
            backupDir.mkdirs()
        }

        val sourceFile = File(sourceUri.path ?: return)
        val destFile = File(backupDir, sourceFile.name)

        sourceFile.copyTo(destFile, overwrite = true)
        sourceFile.delete()
    }

    private fun cleanOldBackups() {
        val backupDir = File(context.getExternalFilesDir(null), BACKUP_DIR)
        if (!backupDir.exists()) return

        val backups = backupDir.listFiles()
            ?.sortedByDescending { it.lastModified() }
            ?: return

        // Keep only the most recent backups
        backups.drop(MAX_BACKUPS).forEach { it.delete() }
    }

    // Get list of available backups
    fun getAvailableBackups(): List<File> {
        val backupDir = File(context.getExternalFilesDir(null), BACKUP_DIR)
        if (!backupDir.exists()) return emptyList()

        return backupDir.listFiles()
            ?.sortedByDescending { it.lastModified() }
            ?.toList()
            ?: emptyList()
    }

    // Get backup info
    fun getBackupInfo(file: File): BackupInfo {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return BackupInfo(
            fileName = file.name,
            date = dateFormat.format(Date(file.lastModified())),
            size = formatFileSize(file.length())
        )
    }

    private fun formatFileSize(bytes: Long): String {
        return when {
            bytes < 1024 -> "$bytes B"
            bytes < 1024 * 1024 -> "${bytes / 1024} KB"
            else -> "${bytes / (1024 * 1024)} MB"
        }
    }
}

data class BackupInfo(
    val fileName: String,
    val date: String,
    val size: String
)
