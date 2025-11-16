package com.example.drinkup.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.drinkup.databinding.ActivitySplashBinding
import com.example.drinkup.auth.LoginActivity
import com.example.drinkup.main.MainActivity
import com.example.drinkup.onboarding.OnboardingActivity
import com.example.drinkup.utils.PreferencesManager

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var prefsManager: PreferencesManager // ⚠ ici ajouté

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefsManager = PreferencesManager(this)

        // Animation de chargement déjà dans le XML
        Handler(Looper.getMainLooper()).postDelayed({
            navigateToNextScreen()
        }, 2000)
    }

    private fun navigateToNextScreen() {
        val intent = when {
            prefsManager.isFirstRun -> Intent(this, OnboardingActivity::class.java)
            prefsManager.currentUserId == -1L -> Intent(this, LoginActivity::class.java) // 🔹 currentUserId
            else -> Intent(this, MainActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}
