package com.example.ketofit.data.repository

import com.example.ketofit.data.model.ProgressData
import com.example.ketofit.data.remote.ApiService

class ProgressRepository(
    private val apiService: ApiService,
) {
    suspend fun getProgress(): ProgressData {
        val response = apiService.getProgress()
        if (!response.success || response.data == null) {
            throw IllegalStateException(response.message ?: "Load progress failed")
        }
        return response.data
    }

    suspend fun addWeight(weight: Double): ProgressData {
        val response = apiService.addWeight(weight)
        if (!response.success || response.data == null) {
            throw IllegalStateException(response.message ?: "Add weight failed")
        }
        return response.data
    }
}

