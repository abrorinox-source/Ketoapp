package com.example.ketofit.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.ketofit.AppContainer
import com.example.ketofit.navigation.BottomNavItem
import com.example.ketofit.ui.components.BottomNavigationBar

@Composable
fun MainScaffold(
    container: AppContainer,
    onLogout: () -> Unit,
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            BottomNavigationBar(
                selectedIndex = selectedTab,
                onTabSelected = { selectedTab = it },
            )
        },
    ) { padding ->
        when (BottomNavItem.entries[selectedTab]) {
            BottomNavItem.Home -> DashboardScreen(
                container = container,
                modifier = Modifier.padding(padding),
                onNavigateToTab = { selectedTab = it },
            )
            BottomNavItem.Lessons -> LessonsScreen(container = container, modifier = Modifier.padding(padding))
            BottomNavItem.Menu -> MenuScreen(container = container, modifier = Modifier.padding(padding))
            BottomNavItem.Progress -> ProgressScreen(container = container, modifier = Modifier.padding(padding))
            BottomNavItem.Profile -> ProfileScreen(container = container, modifier = Modifier.padding(padding), onLogout = onLogout)
        }
    }
}
