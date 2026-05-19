package com.example.ketofit.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ketofit.data.model.DashboardBundle
import com.example.ketofit.data.repository.UserRepository
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {
    var isLoading by mutableStateOf(false)
        private set
    var dashboard by mutableStateOf<DashboardBundle?>(null)
        private set

    fun loadDashboard() {
        isLoading = true
        viewModelScope.launch {
            dashboard = userRepository.getDashboard()
            isLoading = false
        }
    }
}

