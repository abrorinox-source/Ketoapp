package com.example.ketofit.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ketofit.data.model.AccessCode
import com.example.ketofit.data.model.Subscription
import com.example.ketofit.data.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set
    var accessCode by mutableStateOf<AccessCode?>(null)
        private set
    var subscription by mutableStateOf<Subscription?>(null)
        private set

    fun activateCode(code: String, onSuccess: () -> Unit) {
        isLoading = true
        errorMessage = null
        viewModelScope.launch {
            try {
                accessCode = authRepository.activateAccessCode(code)
                subscription = authRepository.currentSubscription()
                onSuccess()
            } catch (e: Exception) {
                errorMessage = e.message ?: "Activation failed"
            } finally {
                isLoading = false
            }
        }
    }
}

