package com.example.ketofit.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ketofit.data.model.ProgressData
import com.example.ketofit.data.repository.ProgressRepository
import kotlinx.coroutines.launch

class ProgressViewModel(
    private val progressRepository: ProgressRepository,
) : ViewModel() {
    var isLoading by mutableStateOf(false)
        private set
    var progress by mutableStateOf<ProgressData?>(null)
        private set

    fun loadProgress() {
        isLoading = true
        viewModelScope.launch {
            progress = progressRepository.getProgress()
            isLoading = false
        }
    }

    fun addWeight(weight: Double) {
        viewModelScope.launch {
            progress = progressRepository.addWeight(weight)
            isLoading = false
        }
    }
}

