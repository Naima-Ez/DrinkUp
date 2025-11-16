package com.example.drinkup

import com.example.drinkup.utils.isValidEmail
import com.example.drinkup.utils.isValidPassword
import com.example.drinkup.utils.toLiters
import com.example.drinkup.utils.toMilliliters
import org.junit.Test
import org.junit.Assert.*


class ExtensionsTest {

    @Test
    fun testIsValidEmail_validEmail_returnsTrue() {
        assertTrue("test@example.com".isValidEmail())
    }

    @Test
    fun testIsValidEmail_invalidEmail_returnsFalse() {
        assertFalse("invalid-email".isValidEmail())
        assertFalse("test@".isValidEmail())
        assertFalse("@example.com".isValidEmail())
    }

    @Test
    fun testIsValidPassword_validPassword_returnsTrue() {
        assertTrue("password123".isValidPassword())
        assertTrue("123456".isValidPassword())
    }

    @Test
    fun testIsValidPassword_tooShort_returnsFalse() {
        assertFalse("12345".isValidPassword())
        assertFalse("abc".isValidPassword())
    }

    @Test
    fun testToLiters_convertsCorrectly() {
        assertEquals(1.0f, 1000.toLiters(), 0.01f)
        assertEquals(0.5f, 500.toLiters(), 0.01f)
        assertEquals(2.5f, 2500.toLiters(), 0.01f)
    }

    @Test
    fun testToMilliliters_convertsCorrectly() {
        assertEquals(1000, 1.0f.toMilliliters())
        assertEquals(500, 0.5f.toMilliliters())
        assertEquals(2500, 2.5f.toMilliliters())
    }
}