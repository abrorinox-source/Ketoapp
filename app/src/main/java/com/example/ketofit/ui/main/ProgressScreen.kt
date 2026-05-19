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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ketofit.AppContainer
import com.example.ketofit.ui.components.AppButton
import com.example.ketofit.ui.components.AppTextField
import com.example.ketofit.ui.components.ProgressCard
import com.example.ketofit.ui.components.WeightChart
import com.example.ketofit.viewmodel.ProgressViewModel

@Composable
fun ProgressScreen(
    container: AppContainer,
    modifier: Modifier = Modifier,
) {
    val viewModel: ProgressViewModel = viewModel(factory = container.viewModelFactory)
    var weight by remember { mutableStateOf("") }
    LaunchedEffect(Unit) { viewModel.loadProgress() }

    val progress = viewModel.progress
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(text = "Progress", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        if (viewModel.isLoading && progress == null) {
            CircularProgressIndicator()
        }
        progress?.let {
            ProgressCard(
                title = "Vazn holati",
                progress = ((it.lostKg / ((it.startWeightKg - it.targetWeightKg).takeIf { diff -> diff > 0 } ?: 1.0)) * 100).toInt().coerceIn(0, 100),
                currentWeight = it.currentWeightKg,
                targetWeight = it.targetWeightKg,
            )
            WeightChart(weights = it.lastWeekWeights)
        }
        AppTextField(
            value = weight,
            onValueChange = { weight = it },
            label = "Bugungi vazn",
            placeholder = "84.8",
        )
        AppButton(text = "Saqlash") {
            weight.toDoubleOrNull()?.let { viewModel.addWeight(it) }
        }
    }
}
