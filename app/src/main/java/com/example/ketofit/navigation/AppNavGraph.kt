package com.example.ketofit.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ketofit.AppContainer
import com.example.ketofit.ui.main.MainScaffold
import com.example.ketofit.ui.onboarding.GoalSelectionScreen
import com.example.ketofit.ui.onboarding.LanguageScreen
import com.example.ketofit.ui.onboarding.ProfileSetupScreen

@Composable
fun AppNavGraph(
    container: AppContainer,
    navController: NavHostController = rememberNavController(),
) {
    val initialRoute = remember(container.userPreferences.onboardingCompleted) {
        if (container.userPreferences.onboardingCompleted) {
            Routes.MAIN
        } else {
            Routes.LANGUAGE
        }
    }

    NavHost(
        navController = navController,
        startDestination = initialRoute,
    ) {
        composable(Routes.LANGUAGE) {
            LanguageScreen(
                container = container,
                onContinue = { navController.navigate(Routes.PROFILE_SETUP) },
            )
        }
        composable(Routes.PROFILE_SETUP) {
            ProfileSetupScreen(
                container = container,
                onContinue = { navController.navigate(Routes.GOAL_SELECTION) },
            )
        }
        composable(Routes.GOAL_SELECTION) {
            GoalSelectionScreen(
                container = container,
                onContinue = {
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.LANGUAGE) { inclusive = true }
                    }
                },
            )
        }
        composable(Routes.MAIN) {
            MainScaffold(
                container = container,
                onLogout = {
                    navController.navigate(Routes.LANGUAGE) {
                        popUpTo(Routes.MAIN) { inclusive = true }
                        launchSingleTop = true
                    }
                },
            )
        }
    }
}
