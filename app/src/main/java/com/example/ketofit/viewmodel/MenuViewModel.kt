package com.example.ketofit.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ketofit.data.model.Menu
import com.example.ketofit.data.repository.MenuRepository
import kotlinx.coroutines.launch

class MenuViewModel(
    private val menuRepository: MenuRepository,
) : ViewModel() {
    var isLoading by mutableStateOf(false)
        private set
    var menu by mutableStateOf<Menu?>(null)
        private set

    fun loadTodayMenu() {
        isLoading = true
        viewModelScope.launch {
            menu = menuRepository.getTodayMenu()
            isLoading = false
        }
    }
}

