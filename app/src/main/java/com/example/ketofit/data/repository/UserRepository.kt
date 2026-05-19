package com.example.ketofit.data.repository

import com.example.ketofit.data.local.UserPreferences
import com.example.ketofit.data.model.DashboardBundle
import com.example.ketofit.data.model.User
import com.example.ketofit.data.remote.ApiService

class UserRepository(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences,
) {
    suspend fun saveProfile(user: User): User {
        val response = apiService.saveProfile(user)
        if (!response.success || response.data == null) {
            throw IllegalStateException(response.message ?: "Save profile failed")
        }
        userPreferences.saveProfile(
            name = user.name,
            gender = user.gender,
            age = user.age,
            height = user.heightCm,
            currentWeight = user.currentWeightKg,
            targetWeight = user.targetWeightKg,
        )
        return response.data
    }

    suspend fun getProfile(): User {
        val response = apiService.getProfile()
        if (!response.success || response.data == null) {
            throw IllegalStateException(response.message ?: "Load profile failed")
        }
        return response.data
    }

    suspend fun updateProfile(user: User): User {
        val response = apiService.updateProfile(user)
        if (!response.success || response.data == null) {
            throw IllegalStateException(response.message ?: "Update profile failed")
        }
        return response.data
    }

    suspend fun getDashboard(): DashboardBundle {
        val response = apiService.getDashboard()
        if (!response.success || response.data == null) {
            throw IllegalStateException(response.message ?: "Load dashboard failed")
        }
        return response.data
    }
}

