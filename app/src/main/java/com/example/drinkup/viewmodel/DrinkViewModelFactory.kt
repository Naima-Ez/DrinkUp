package com.example.drinkup

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.drinkup.database.entities.UserProfile

class DrinkViewModelImproved(application: Application) : AndroidViewModel(application) {

    private val _currentUser = MutableLiveData<UserProfile?>()
    val currentUser: LiveData<UserProfile?> get() = _currentUser

    fun setCurrentUser(user: UserProfile) {
        _currentUser.value = user
    }

    fun updateNotifications(enabled: Boolean) {
        _currentUser.value = _currentUser.value?.copy(notificationsEnabled = enabled)
    }

    fun updateLanguage(lang: String) {
        _currentUser.value = _currentUser.value?.copy(langue = lang)
    }

    // أي دوال إضافية بحال updateGoal، insertDrink… يمكن تضيفها هنا
}
