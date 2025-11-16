package com.example.drinkup.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.drinkup.database.AppDatabase
import com.example.drinkup.database.DailySummary
import com.example.drinkup.database.entities.DrinkEntry
import com.example.drinkup.database.entities.UserProfile
import com.example.drinkup.repository.DrinkRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class DrinkViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).drinkDao()
    private val repository = DrinkRepository(dao)

    // -------------------------
    // CURRENT USER
    // -------------------------
    private val _currentUser = MutableLiveData<UserProfile?>()
    val currentUser: LiveData<UserProfile?> = _currentUser

    // -------------------------
    // TODAY TOTAL
    // -------------------------
    private val _todayTotal = MutableLiveData(0)
    val todayTotal: LiveData<Int> = _todayTotal

    // -------------------------
    // GOAL PROGRESS
    // -------------------------
    private val _goalProgress = MutableLiveData<Float>()
    val goalProgress: LiveData<Float> = _goalProgress

    // -------------------------
    // LOGIN
    // -------------------------
    fun loginUser(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val user = repository.loginUser(email, password)
            if (user != null) {
                _currentUser.postValue(user)
                updateTodayTotal()
                onSuccess()
            } else {
                onError("Email or password incorrect")
            }
        }
    }

    // -------------------------
    // SET CURRENT USER
    // -------------------------
    fun setCurrentUser(userId: Long) {
        viewModelScope.launch {
            _currentUser.value = repository.getUserById(userId)
            updateTodayTotal()
        }
    }

    // -------------------------
    // REGISTER USER
    // -------------------------
    fun registerUser(user: UserProfile, onSuccess: (Int) -> Unit) {
        viewModelScope.launch {
            val userId = repository.insertUser(user).toInt()
            user.id = userId.toLong()
            _currentUser.postValue(user)
            updateTodayTotal()
            onSuccess(userId)
        }
    }

    // -------------------------
    // ADD DRINK
    // -------------------------
    fun addDrink(quantityMl: Int) {
        val user = _currentUser.value ?: return
        val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

        val drink = DrinkEntry(userId = user.id, quantiteMl = quantityMl, date = date)

        viewModelScope.launch {
            repository.insertDrink(drink)
            updateTodayTotal()
        }
    }

    // -------------------------
    // TODAY TOTAL + GOAL PROGRESS
    // -------------------------
    private fun updateTodayTotal() {
        val user = _currentUser.value ?: return
        val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

        viewModelScope.launch {
            val total = repository.getTotalByDate(user.id, date)
            _todayTotal.postValue(total)
            updateGoalProgress(total, user)
        }
    }

    private fun updateGoalProgress(total: Int, user: UserProfile) {
        _goalProgress.postValue(
            if (user.objectifMl > 0) (total.toFloat() / user.objectifMl) * 100f else 0f
        )
    }

    fun getProgressPercentage(): Int {
        val total = _todayTotal.value ?: 0
        val user = _currentUser.value ?: return 0
        return if (user.objectifMl > 0) (total * 100 / user.objectifMl) else 0
    }

    // -------------------------
    // UPDATE GOAL
    // -------------------------
    fun updateGoal(newGoalMl: Int) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.updateGoal(user.id, newGoalMl)
            user.objectifMl = newGoalMl
            _currentUser.postValue(user)
            updateTodayTotal()
        }
    }

    // -------------------------
    // NOTIFICATIONS
    // -------------------------
    fun updateNotifications(enabled: Boolean) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.updateNotifications(user.id, enabled)
            user.notificationsEnabled = enabled
            _currentUser.postValue(user)
        }
    }

    // -------------------------
    // LANGUAGE
    // -------------------------
    fun updateLanguage(lang: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.updateLanguage(user.id, lang)
            user.langue = lang
            _currentUser.postValue(user)
        }
    }

    // -------------------------
    // GOAL HISTORY
    // -------------------------
    fun getGoalHistory() = _currentUser.value?.let {
        repository.getGoalHistoryLive(it.id)
    } ?: MutableLiveData(emptyList())

    // -------------------------
    // WEEKLY SUMMARY
    // -------------------------
    suspend fun getWeeklySummary(): List<DailySummary> {
        val user = _currentUser.value ?: return emptyList()
        return repository.getWeeklySummary(user.id)
    }
}
