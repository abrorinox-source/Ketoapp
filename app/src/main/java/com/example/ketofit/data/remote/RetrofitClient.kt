package com.example.ketofit.data.remote

import com.example.ketofit.data.local.UserPreferences

object RetrofitClient {
    fun createApiService(userPreferences: UserPreferences): ApiService {
        return ApiService(userPreferences)
    }
}
