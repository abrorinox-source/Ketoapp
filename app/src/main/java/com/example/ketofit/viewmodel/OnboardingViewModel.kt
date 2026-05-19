package com.example.ketofit.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ketofit.data.local.UserPreferences
import com.example.ketofit.data.model.Subscription
import com.example.ketofit.data.model.User
import com.example.ketofit.data.repository.UserRepository
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val userRepository: UserRepository,
    private val userPreferences: UserPreferences,
) : ViewModel() {
    var language by mutableStateOf(userPreferences.selectedLanguage)
        private set
    var goal by mutableStateOf(userPreferences.goal)
        private set
    var isLoading by mutableStateOf(false)
        private set

    fun saveLanguage(value: String) {
        language = value
        userPreferences.setLanguage(value)
    }

    fun saveProfile(
        name: String,
        gender: String,
        age: Int,
        heightCm: Double,
        currentWeightKg: Double,
        targetWeightKg: Double,
        onDone: () -> Unit,
    ) {
        isLoading = true
        viewModelScope.launch {
            val user = User(
                id = userPreferences.userId ?: "user_001",
                name = name,
                email = "${name.trim().ifBlank { "user" }.lowercase().replace(" ", ".")}@gmail.com",
                language = userPreferences.selectedLanguage,
                gender = gender,
                age = age,
                heightCm = heightCm,
                currentWeightKg = currentWeightKg,
                targetWeightKg = targetWeightKg,
                goal = goal,
                subscription = Subscription(active = true, planName = "30 kunlik keto kurs"),
                reminderEnabled = true,
            )
            userRepository.saveProfile(user)
            isLoading = false
            onDone()
        }
    }

    fun saveGoal(value: String, onDone: () -> Unit) {
        goal = value
        userPreferences.saveGoal(value)
        userPreferences.setOnboardingCompleted(true)
        onDone()
    }
}

