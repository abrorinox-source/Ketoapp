package com.example.ketofit.data.repository

import com.example.ketofit.data.local.UserPreferences
import com.example.ketofit.data.model.AccessCode
import com.example.ketofit.data.model.Subscription
import com.example.ketofit.data.remote.ApiService

class AuthRepository(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences,
) {
    suspend fun activateAccessCode(code: String): AccessCode {
        val response = apiService.activateAccessCode(code)
        if (!response.success || response.data == null) {
            throw IllegalStateException(response.message ?: "Activation failed")
        }
        userPreferences.setSubscriptionActive(true)
        userPreferences.saveSession(userId = "user_001", accessToken = "mock-token")
        return response.data
    }

    fun currentSubscription(): Subscription = apiService.currentSubscription()
}

