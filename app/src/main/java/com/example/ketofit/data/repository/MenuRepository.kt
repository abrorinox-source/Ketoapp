package com.example.ketofit.data.repository

import com.example.ketofit.data.model.Menu
import com.example.ketofit.data.remote.ApiService

class MenuRepository(
    private val apiService: ApiService,
) {
    suspend fun getTodayMenu(): Menu {
        val response = apiService.getTodayMenu()
        if (!response.success || response.data == null) {
            throw IllegalStateException(response.message ?: "Load menu failed")
        }
        return response.data
    }
}

