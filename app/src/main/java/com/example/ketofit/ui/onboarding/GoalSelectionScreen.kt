package com.example.ketofit.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.RadioButton
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
import com.example.ketofit.ui.theme.PrimaryContainer
import com.example.ketofit.ui.theme.SurfaceHighest
import com.example.ketofit.viewmodel.OnboardingViewModel

@Composable
fun GoalSelectionScreen(
    container: AppContainer,
    onContinue: () -> Unit,
) {
    val viewModel: OnboardingViewModel = viewModel(factory = container.viewModelFactory)
    var selectedGoal by remember { mutableStateOf("Ozish") }
    val goals = listOf(
        GoalUi("Ozish", "Vazn kamaytirish va yog' yoqish", "01"),
        GoalUi("Vaznni ushlab turish", "Hozirgi natijani saqlab qolish", "02"),
        GoalUi("Sog'lom ovqatlanish", "Low-carb odatlarini shakllantirish", "03"),
        GoalUi("Keto'ni o'rganish", "Boshlovchilar uchun oddiy darslar", "04"),
    )

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
                    modifier = Modifier
                        .size(80.dp)
                        .padding(8.dp)
                        .clip(CircleShape)
                        .background(PrimaryContainer),
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Maqsadingizni tanlang",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "Sizga mos keto reja tayyorlashimiz uchun asosiy maqsadingizni belgilang.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                goals.forEach { goal ->
                    GoalCard(
                        goal = goal,
                        selected = selectedGoal == goal.title,
                        onClick = { selectedGoal = goal.title },
                    )
                }
            }
        }
        AppButton(text = "Davom etish") {
            viewModel.saveGoal(selectedGoal) {
                onContinue()
            }
        }
    }
}

private data class GoalUi(
    val title: String,
    val subtitle: String,
    val code: String,
)

@Composable
private fun GoalCard(
    goal: GoalUi,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = if (selected) 6.dp else 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) PrimaryContainer else SurfaceHighest,
                shape = RoundedCornerShape(20.dp),
            )
            .clickable(onClick = onClick),
    ) {
        androidx.compose.foundation.layout.Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            androidx.compose.foundation.layout.Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.weight(1f),
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(PrimaryContainer),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = goal.code, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
                Column {
                    Text(text = goal.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text(text = goal.subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            RadioButton(selected = selected, onClick = onClick)
        }
    }
}
