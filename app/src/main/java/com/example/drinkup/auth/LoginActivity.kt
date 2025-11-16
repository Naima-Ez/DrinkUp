package com.example.drinkup.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.drinkup.R
import com.example.drinkup.databinding.ActivityLoginBinding
import com.example.drinkup.main.MainActivity
import com.example.drinkup.utils.PreferencesManager
import com.example.drinkup.viewmodel.DrinkViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: DrinkViewModel by viewModels()
    private lateinit var prefsManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefsManager = PreferencesManager(this)

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnLogin.setOnClickListener {
            performLogin()
        }

        binding.tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun performLogin() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()

        // Validation
        if (email.isEmpty()) {
            binding.tilEmail.error = getString(R.string.error_email_required)
            return
        }

        if (password.isEmpty()) {
            binding.tilPassword.error = getString(R.string.error_password_required)
            return
        }

        // Clear errors
        binding.tilEmail.error = null
        binding.tilPassword.error = null

        // Disable button during login
        binding.btnLogin.isEnabled = false

        viewModel.loginUser(
            email = email,
            password = password,
            onSuccess = {
                val userId = viewModel.currentUser.value?.id ?: -1L
                prefsManager.currentUserId = userId

                Toast.makeText(
                    this,
                    getString(R.string.login_success),
                    Toast.LENGTH_SHORT
                ).show()

                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            },
            onError = { error ->
                binding.btnLogin.isEnabled = true
                Toast.makeText(this, error, Toast.LENGTH_LONG).show()
            }
        )
    }
}
