package com.example.drinkup

import android.content.Context
import com.example.drinkup.utils.ValidationResult
import com.example.drinkup.utils.ValidationUtils
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
// Robolectric



@RunWith(RobolectricTestRunner::class)
class ValidationUtilsTest {

    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun testValidateEmail_validEmail_returnsSuccess() {
        val result = ValidationUtils.validateEmail("test@example.com", context)
        assertTrue(result is ValidationResult.Success)
    }

    @Test
    fun testValidateEmail_emptyEmail_returnsError() {
        val result = ValidationUtils.validateEmail("", context)
        assertTrue(result is ValidationResult.Error)
    }

    @Test
    fun testValidatePassword_validPassword_returnsSuccess() {
        val result = ValidationUtils.validatePassword("password123", context)
        assertTrue(result is ValidationResult.Success)
    }

    @Test
    fun testValidatePassword_tooShort_returnsError() {
        val result = ValidationUtils.validatePassword("12345", context)
        assertTrue(result is ValidationResult.Error)
    }

    @Test
    fun testValidateGoal_validGoal_returnsSuccess() {
        val result = ValidationUtils.validateGoal(2.5f, context)
        assertTrue(result is ValidationResult.Success)
    }

    @Test
    fun testValidateGoal_tooLarge_returnsError() {
        val result = ValidationUtils.validateGoal(15f, context)
        assertTrue(result is ValidationResult.Error)
    }
}