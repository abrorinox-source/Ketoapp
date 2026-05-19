package com.example.ketofit.ui.main

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ketofit.AppContainer
import com.example.ketofit.ui.components.AppButton
import com.example.ketofit.ui.components.AppCard
import com.example.ketofit.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    container: AppContainer,
    modifier: Modifier = Modifier,
    onLogout: () -> Unit,
) {
    val viewModel: ProfileViewModel = viewModel(factory = container.viewModelFactory)
    LaunchedEffect(Unit) { viewModel.loadProfile() }
    val profile = viewModel.profile

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(text = "Profil", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        if (profile == null) {
            CircularProgressIndicator()
        } else {
            AppCard {
                Text(text = profile.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                Text(text = profile.email)
                Text(text = "Til: ${profile.language}")
                Text(text = "Maqsad: ${profile.goal}")
                Text(text = if (profile.subscription.active) "Premium faol" else "Free")
            }
            AppCard {
                Text(text = "Shaxsiy ma'lumotlar", fontWeight = FontWeight.SemiBold)
                Text(text = "Jins: ${profile.gender}")
                Text(text = "Yosh: ${profile.age}")
                Text(text = "Bo'y: ${profile.heightCm} cm")
                Text(text = "Vazn: ${profile.currentWeightKg} kg")
                Text(text = "Maqsad vazn: ${profile.targetWeightKg} kg")
            }
        }
        AppButton(text = "Chiqish") {
            viewModel.logout(onLogout)
        }
    }
}
