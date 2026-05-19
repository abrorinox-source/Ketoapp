package com.example.ketofit.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.RestaurantMenu
import androidx.compose.material.icons.outlined.School
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomNavItem(val label: String, val icon: ImageVector) {
    Home("Home", Icons.Outlined.Home),
    Lessons("Darslar", Icons.Outlined.School),
    Menu("Menyu", Icons.Outlined.RestaurantMenu),
    Progress("Progress", Icons.Outlined.Insights),
    Profile("Profil", Icons.Outlined.Person),
}
