package com.example.drinkup.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    // ترجع العمر من تاريخ الولادة
    fun calculateAge(birthDate: String): Int {
        val dob = dateFormat.parse(birthDate) ?: return 0
        val dobCalendar = Calendar.getInstance().apply { time = dob }
        val today = Calendar.getInstance()

        var age = today.get(Calendar.YEAR) - dobCalendar.get(Calendar.YEAR)
        if (today.get(Calendar.DAY_OF_YEAR) < dobCalendar.get(Calendar.DAY_OF_YEAR)) age--
        return age
    }

    // آخر 7 أيام
    fun getLastSevenDays(): List<String> {
        val calendar = Calendar.getInstance()
        val days = mutableListOf<String>()
        for (i in 6 downTo 0) {
            calendar.time = Date()
            calendar.add(Calendar.DAY_OF_YEAR, -i)
            days.add(dateFormat.format(calendar.time))
        }
        return days
    }

    // اليوم من الأسبوع من تاريخ معين
    fun getDayOfWeek(dateStr: String): String {
        val date = dateFormat.parse(dateStr) ?: Date()
        val sdf = SimpleDateFormat("EEE", Locale.getDefault())
        return sdf.format(date)
    }
}
