package com.example.ketofit.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ketofit.data.local.UserPreferences
import com.example.ketofit.data.model.User
import com.example.ketofit.data.repository.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val userPreferences: UserPreferences,
) : ViewModel() {
    var profile by mutableStateOf<User?>(null)
        private set

    fun loadProfile() {
        viewModelScope.launch {
            profile = userRepository.getProfile()
        }
    }

    fun updateProfile(user: User) {
        viewModelScope.launch {
            profile = userRepository.updateProfile(user)
        }
    }

    fun logout(onDone: () -> Unit) {
        userPreferences.clearSession()
        profile = null
        onDone()
    }
}

