package com.example.drinkup.utils

import android.content.Context
import com.example.drinkup.R

object ValidationUtils {

    fun validateEmail(email: String, context: Context): ValidationResult {
        return when {
            email.isEmpty() -> ValidationResult.Error(context.getString(R.string.error_email_required))
            !email.isValidEmail() -> ValidationResult.Error(context.getString(R.string.error_email_invalid))
            else -> ValidationResult.Success
        }
    }

    fun validatePassword(password: String, context: Context): ValidationResult {
        return when {
            password.isEmpty() -> ValidationResult.Error(context.getString(R.string.error_password_required))
            !password.isValidPassword() -> ValidationResult.Error(context.getString(R.string.error_password_length))
            else -> ValidationResult.Success
        }
    }

    fun validateName(name: String, context: Context): ValidationResult {
        return when {
            name.isEmpty() -> ValidationResult.Error(context.getString(R.string.error_name_required))
            name.length < 2 -> ValidationResult.Error("Le nom doit contenir au moins 2 caractères")
            else -> ValidationResult.Success
        }
    }

    fun validateBirthDate(birthDate: String, context: Context): ValidationResult {
        if (birthDate.isEmpty()) {
            return ValidationResult.Error(context.getString(R.string.error_birthdate_required))
        }

        val age = DateUtils.calculateAge(birthDate)
        return when {
            age < 18 -> ValidationResult.Error(context.getString(R.string.error_age_minimum))
            age > 120 -> ValidationResult.Error("Date de naissance invalide")
            else -> ValidationResult.Success
        }
    }

    fun validateGoal(goalLiters: Float, context: Context): ValidationResult {
        return when {
            goalLiters <= 0 -> ValidationResult.Error(context.getString(R.string.error_invalid_goal))
            goalLiters > 10 -> ValidationResult.Error("L'objectif ne peut pas dépasser 10L")
            else -> ValidationResult.Success
        }
    }
}

sealed class ValidationResult {
    object Success : ValidationResult()
    data class Error(val message: String) : ValidationResult()
}