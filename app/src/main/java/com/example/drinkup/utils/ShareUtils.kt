package com.example.drinkup.utils


import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File

object ShareUtils {

    fun shareText(context: Context, title: String, text: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, title)
            putExtra(Intent.EXTRA_TEXT, text)
        }

        context.startActivity(Intent.createChooser(intent, "Partager via"))
    }

    fun shareProgress(context: Context, todayLiters: String, goalLiters: String, percentage: Int) {
        val text = """
            🩵 DrinkUp - Mon hydratation
            
            Aujourd'hui: $todayLiters L
            Objectif: $goalLiters L
            Progression: $percentage%
            
            ${if (percentage >= 100) "✅ Objectif atteint!" else "💪 En cours..."}
            
            #DrinkUp #Hydratation #Santé
        """.trimIndent()

        shareText(context, "Mon hydratation DrinkUp", text)
    }
}