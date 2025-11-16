package com.example.drinkup.auth

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.drinkup.databinding.ActivityRegisterBinding
import com.example.drinkup.R
import com.example.drinkup.database.entities.UserProfile
import com.example.drinkup.utils.PreferencesManager
import com.example.drinkup.utils.DateUtils
import com.example.drinkup.main.MainActivity
import com.example.drinkup.viewmodel.DrinkViewModel
import java.util.*
import kotlin.String

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: DrinkViewModel by viewModels() // ✅ AndroidViewModel
    private lateinit var prefsManager: PreferencesManager
    private var selectedDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefsManager = PreferencesManager(this)

        setupListeners()
    }

    private fun setupListeners() {
        binding.etBirthDate.setOnClickListener {
            showDatePicker()
        }

        binding.btnRegister.setOnClickListener {
            performRegistration()
        }

        binding.tvLogin.setOnClickListener {
            finish() // العودة لصفحة تسجيل الدخول
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.YEAR, -18) // Default to 18 years ago

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                binding.etBirthDate.setText(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun performRegistration() {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()
        val birthDate = selectedDate

        // Validation
        var hasError = false

        if (name.isEmpty()) {
            binding.tilName.error = getString(R.string.error_name_required)
            hasError = true
        } else binding.tilName.error = null

        if (email.isEmpty()) {
            binding.tilEmail.error = getString(R.string.error_email_required)
            hasError = true
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = getString(R.string.error_email_invalid)
            hasError = true
        } else binding.tilEmail.error = null

        if (password.isEmpty()) {
            binding.tilPassword.error = getString(R.string.error_password_required)
            hasError = true
        } else if (password.length < 6) {
            binding.tilPassword.error = getString(R.string.error_password_length)
            hasError = true
        } else binding.tilPassword.error = null

        if (birthDate.isEmpty()) {
            binding.tilBirthDate.error = getString(R.string.error_birthdate_required)
            hasError = true
        } else binding.tilBirthDate.error = null

        if (hasError) return

        // Check age
        val age = DateUtils.calculateAge(birthDate)
        if (age < 18) {
            Toast.makeText(
                this,
                getString(R.string.error_age_minimum),
                Toast.LENGTH_LONG
            ).show()
            return
        }

        binding.btnRegister.isEnabled = false

        val user = UserProfile(
            nom = name,
            username = password,// بدل username → nom
            email = email,
            password = password,
            birthDate = birthDate,
            objectifMl = 2000,        // قيمة افتراضية
            notificationsEnabled = true,
            langue = "fr"
        )

        viewModel.registerUser(user) { userId ->
            Toast.makeText(this, "Success: $userId", Toast.LENGTH_SHORT).show()
        }


        Toast.makeText(
                    this,
                    getString(R.string.register_success),
                    Toast.LENGTH_SHORT
                ).show()

                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } }
