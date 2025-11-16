package com.example.drinkup

import com.example.drinkup.utils.DateUtils
import org.junit.Test
import org.junit.Assert.*


class DateUtilsTest {

    @Test
    fun testCalculateAge_validDate_returnsCorrectAge() {
        val birthDate = "01/01/2000"
        val age = DateUtils.calculateAge(birthDate)
        // العمر المتوقع بين 23 و 25 حسب السنة الحالية (2025)
        assertTrue("Age should be between 23 and 25", age in 23..25)
    }

    @Test
    fun testCalculateAge_underAge_returnsLessThan18() {
        val birthDate = "01/01/2010"
        val age = DateUtils.calculateAge(birthDate)
        assertTrue("Age should be less than 18", age < 18)
    }

    @Test
    fun testGetLastSevenDays_returnsSevenDates() {
        val dates = DateUtils.getLastSevenDays()
        assertEquals("Should return 7 dates", 7, dates.size)
    }

    @Test
    fun testGetDayOfWeek_validDate_returnsDay() {
        val date = "01/01/2024"
        val day = DateUtils.getDayOfWeek(date)
        assertNotNull("Day of week should not be null", day)
        assertTrue("Day of week should not be empty", day.isNotEmpty())
    }
}