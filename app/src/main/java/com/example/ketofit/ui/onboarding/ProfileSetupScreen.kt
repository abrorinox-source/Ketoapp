package com.example.ketofit.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ketofit.AppContainer
import com.example.ketofit.ui.components.AppButton
import com.example.ketofit.ui.components.AppTextField
import com.example.ketofit.ui.theme.PrimaryContainer
import com.example.ketofit.viewmodel.OnboardingViewModel

@Composable
fun ProfileSetupScreen(
    container: AppContainer,
    onContinue: () -> Unit,
) {
    val viewModel: OnboardingViewModel = viewModel(factory = container.viewModelFactory)
    var name by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Ayol") }
    var age by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var currentWeight by remember { mutableStateOf("") }
    var targetWeight by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Surface(shape = CircleShape, color = Color.White, shadowElevation = 4.dp) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(88.dp)
                        .padding(8.dp)
                        .clip(CircleShape)
                        .background(PrimaryContainer)
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Profilingizni sozlaymiz",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "Sizga mos keto reja va progress hisoblash uchun ma'lumotlarni kiriting.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(8.dp))
            ProfileFieldCard {
                AppTextField(value = name, onValueChange = { name = it }, label = "Ism", modifier = Modifier.fillMaxWidth())
                AppTextField(value = gender, onValueChange = { gender = it }, label = "Jins", modifier = Modifier.fillMaxWidth())
                AppTextField(value = age, onValueChange = { age = it }, label = "Yosh", modifier = Modifier.fillMaxWidth())
                AppTextField(value = height, onValueChange = { height = it }, label = "Bo'y, cm", modifier = Modifier.fillMaxWidth())
                AppTextField(value = currentWeight, onValueChange = { currentWeight = it }, label = "Hozirgi vazn, kg", modifier = Modifier.fillMaxWidth())
                AppTextField(value = targetWeight, onValueChange = { targetWeight = it }, label = "Maqsad vazn, kg", modifier = Modifier.fillMaxWidth())
            }
        }
        AppButton(
            text = if (viewModel.isLoading) "Saqlanmoqda..." else "Davom etish",
            enabled = !viewModel.isLoading,
        ) {
            viewModel.saveProfile(
                name = name,
                gender = gender,
                age = age.toIntOrNull() ?: 25,
                heightCm = height.toDoubleOrNull() ?: 170.0,
                currentWeightKg = currentWeight.toDoubleOrNull() ?: 80.0,
                targetWeightKg = targetWeight.toDoubleOrNull() ?: 70.0,
                onDone = onContinue,
            )
        }
    }
}

@Composable
private fun ProfileFieldCard(content: @Composable () -> Unit) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(16.dp),
        ) {
            content()
        }
    }
}
