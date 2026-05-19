package com.example.ketofit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ketofit.AppContainer

class KetoViewModelFactory(
    private val container: AppContainer,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(OnboardingViewModel::class.java) -> OnboardingViewModel(
                userRepository = container.userRepository,
                userPreferences = container.userPreferences,
            )
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(container.authRepository)
            modelClass.isAssignableFrom(DashboardViewModel::class.java) -> DashboardViewModel(container.userRepository)
            modelClass.isAssignableFrom(LessonsViewModel::class.java) -> LessonsViewModel(container.lessonRepository)
            modelClass.isAssignableFrom(MenuViewModel::class.java) -> MenuViewModel(container.menuRepository)
            modelClass.isAssignableFrom(ProgressViewModel::class.java) -> ProgressViewModel(container.progressRepository)
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> ProfileViewModel(
                userRepository = container.userRepository,
                userPreferences = container.userPreferences,
            )
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        } as T
    }
}

