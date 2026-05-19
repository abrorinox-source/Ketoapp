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
import com.example.ketofit.ui.theme.Primary
import com.example.ketofit.ui.theme.PrimaryContainer
import com.example.ketofit.ui.theme.SurfaceContainer
import com.example.ketofit.ui.theme.SurfaceHighest
import com.example.ketofit.viewmodel.OnboardingViewModel

@Composable
fun LanguageScreen(
    container: AppContainer,
    onContinue: () -> Unit,
) {
    val viewModel: OnboardingViewModel = viewModel(factory = container.viewModelFactory)
    var selectedLanguage by remember { mutableStateOf(viewModel.language.ifBlank { "uz" }) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
                .size(220.dp)
                .clip(CircleShape)
                .background(PrimaryContainer.copy(alpha = 0.9f)),
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 56.dp),
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 4.dp,
                ) {
                    Box(
                        modifier = Modifier
                            .size(88.dp)
                            .padding(8.dp)
                            .clip(CircleShape)
                            .background(PrimaryContainer),
                        contentAlignment = Alignment.Center,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Tilni tanlang",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Text(
                    text = "Select your preferred language",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(32.dp))
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    LanguageOption(label = "O'zbek tili", code = "UZ", selected = selectedLanguage == "uz") {
                        selectedLanguage = "uz"
                    }
                    LanguageOption(label = "English", code = "EN", selected = selectedLanguage == "en") {
                        selectedLanguage = "en"
                    }
                    LanguageOption(label = "Русский", code = "RU", selected = selectedLanguage == "ru") {
                        selectedLanguage = "ru"
                    }
                }
            }
            AppButton(
                text = "Davom etish",
                modifier = Modifier.padding(bottom = 24.dp),
            ) {
                viewModel.saveLanguage(selectedLanguage)
                onContinue()
            }
        }
    }
}

@Composable
private fun LanguageOption(
    label: String,
    code: String,
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
        Box(
            modifier = Modifier.padding(16.dp),
        ) {
            androidx.compose.foundation.layout.Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                androidx.compose.foundation.layout.Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(SurfaceContainer),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = code, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Text(text = label, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)
                }
                RadioButton(selected = selected, onClick = onClick)
            }
        }
    }
}
