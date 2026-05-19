package com.example.ketofit

import android.content.Context
import android.content.SharedPreferences
import com.example.ketofit.data.local.UserPreferences
import com.example.ketofit.data.remote.ApiService
import com.example.ketofit.data.repository.AuthRepository
import com.example.ketofit.data.repository.LessonRepository
import com.example.ketofit.data.repository.MenuRepository
import com.example.ketofit.data.repository.ProgressRepository
import com.example.ketofit.data.repository.UserRepository
import com.example.ketofit.viewmodel.KetoViewModelFactory

class AppContainer(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("ketofit_prefs", Context.MODE_PRIVATE)

    val userPreferences = UserPreferences(sharedPreferences)
    val apiService = ApiService()

    val authRepository = AuthRepository(apiService, userPreferences)
    val userRepository = UserRepository(apiService, userPreferences)
    val lessonRepository = LessonRepository(apiService)
    val menuRepository = MenuRepository(apiService)
    val progressRepository = ProgressRepository(apiService)

    val viewModelFactory = KetoViewModelFactory(this)
}

