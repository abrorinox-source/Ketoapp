package com.example.ketofit.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.TrendingDown
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ketofit.AppContainer
import com.example.ketofit.data.model.ProgressData
import com.example.ketofit.data.model.WeightLog
import com.example.ketofit.ui.theme.Primary
import com.example.ketofit.ui.theme.PrimaryContainer
import com.example.ketofit.viewmodel.ProgressViewModel
import java.util.Locale

private val PageBackground = Color(0xFFF8F8F8)
private val CardBorder = Color(0xFFE5E5E5)
private val Ink = Color(0xFF111111)
private val Muted = Color(0xFF555555)
private val SoftMuted = Color(0xFFEEEEEE)
private val GoldText = Color(0xFF765F00)

@Composable
fun ProgressScreen(
    container: AppContainer,
    modifier: Modifier = Modifier,
) {
    val viewModel: ProgressViewModel = viewModel(factory = container.viewModelFactory)
    var weight by remember { mutableStateOf("") }

    LaunchedEffect(Unit) { viewModel.loadProgress() }

    val progress = viewModel.progress
    LaunchedEffect(progress?.currentWeightKg) {
        progress?.let { if (weight.isBlank()) weight = formatWeight(it.currentWeightKg) }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PageBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ProgressHeader()
        TextBlock()

        if (viewModel.isLoading && progress == null) {
            Box(modifier = Modifier.fillMaxWidth().padding(top = 42.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryContainer)
            }
        }

        progress?.let { data ->
            SummaryCard(data = data)
            TodayWeightCard(
                weight = weight,
                onWeightChange = { weight = it },
                onSave = { weight.toDoubleOrNull()?.let(viewModel::addWeight) },
            )
            WeeklyChartCard(data = data)
            MotivationCard()
            WeeklyHistoryCard(history = data.history)
            InfoCard()
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun ProgressHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(PrimaryContainer, CircleShape)
                    .padding(3.dp),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = "K", color = Primary, fontWeight = FontWeight.Black, style = MaterialTheme.typography.titleLarge)
                }
            }
            Text(text = "KETO CLARITY", color = Primary, fontWeight = FontWeight.Black, style = MaterialTheme.typography.titleLarge)
        }
        Icon(imageVector = Icons.Outlined.Notifications, contentDescription = "Notifications", tint = Primary)
    }
}

@Composable
private fun TextBlock() {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(text = "Progress", color = Ink, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
        Text(text = "Vazn va natijalaringizni kuzatib boring.", color = Muted, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun SummaryCard(data: ProgressData) {
    val totalGoal = (data.startWeightKg - data.targetWeightKg).coerceAtLeast(1.0)
    val progress = (data.lostKg / totalGoal).toFloat().coerceIn(0f, 1f)

    ProgressSurface {
        Column(modifier = Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
                Text(text = "X U L O S A", color = Primary, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Medium)
                Text(
                    text = "Siz ${formatWeight(data.lostKg)} kg kamaydingiz",
                    color = Ink,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black,
                )
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                SummaryMetric("Boshlang'ich", data.startWeightKg)
                VerticalDivider()
                SummaryMetric("Hozirgi", data.currentWeightKg, highlighted = true)
                VerticalDivider()
                SummaryMetric("Maqsad", data.targetWeightKg)
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(11.dp)
                        .background(SoftMuted, CircleShape),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress)
                            .height(11.dp)
                            .background(PrimaryContainer, CircleShape),
                    )
                }
                Text(
                    text = "Yana ${formatWeight(data.remainingKg)} kg qoldi",
                    color = Muted,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}

@Composable
private fun SummaryMetric(title: String, value: Double, highlighted: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Text(text = title, color = if (highlighted) Primary else Muted, style = MaterialTheme.typography.bodyMedium, fontWeight = if (highlighted) FontWeight.Bold else FontWeight.Normal)
        Text(text = "${formatWeight(value)} kg", color = Ink, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun VerticalDivider() {
    Box(
        modifier = Modifier
            .height(35.dp)
            .width(1.dp)
            .background(CardBorder),
    )
}

@Composable
private fun TodayWeightCard(
    weight: String,
    onWeightChange: (String) -> Unit,
    onSave: () -> Unit,
) {
    ProgressSurface {
        Column(modifier = Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
            Text(text = "Bugungi vazn", color = Ink, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium)
            Surface(shape = RoundedCornerShape(13.dp), color = Color(0xFFF0F0F0), modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.padding(horizontal = 22.dp, vertical = 15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    BasicTextField(
                        value = weight,
                        onValueChange = onWeightChange,
                        textStyle = TextStyle(color = Ink, fontSize = MaterialTheme.typography.bodyLarge.fontSize),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        decorationBox = { innerTextField ->
                            if (weight.isBlank()) Text(text = "84.8 kg", color = Muted)
                            innerTextField()
                        },
                    )
                    Icon(imageVector = Icons.Outlined.Edit, contentDescription = "Edit weight", tint = Muted)
                }
            }
            Button(
                onClick = onSave,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryContainer, contentColor = Primary),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                modifier = Modifier.fillMaxWidth().height(50.dp),
            ) {
                Text(text = "Saqlash", fontWeight = FontWeight.Black)
            }
        }
    }
}

@Composable
private fun WeeklyChartCard(data: ProgressData) {
    val logs = data.history.takeLast(7)
    val weights = logs.map { it.weightKg }.ifEmpty { listOf(data.currentWeightKg) }
    val maxWeight = weights.maxOrNull() ?: data.currentWeightKg
    val minWeight = weights.minOrNull() ?: data.currentWeightKg
    val range = (maxWeight - minWeight).takeIf { it > 0.01 } ?: 1.0
    val weekDays = listOf("Du", "Se", "Ch", "Pa", "Ju", "Sha", "Ya")
    val delta = (weights.firstOrNull() ?: data.currentWeightKg) - (weights.lastOrNull() ?: data.currentWeightKg)

    ProgressSurface {
        Column(modifier = Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(22.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Oxirgi 7 kunlik grafik", color = Ink, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium)
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = "-${formatWeight(delta.coerceAtLeast(0.0))} kg", color = Primary, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    Icon(imageVector = Icons.Outlined.TrendingDown, contentDescription = null, tint = Primary, modifier = Modifier.size(16.dp))
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().height(118.dp),
                horizontalArrangement = Arrangement.spacedBy(11.dp),
                verticalAlignment = Alignment.Bottom,
            ) {
                weekDays.forEachIndexed { index, day ->
                    val value = weights.getOrNull(index) ?: weights.last()
                    val normalized = ((maxWeight - value) / range).toFloat().coerceIn(0f, 1f)
                    val barHeight = 78 + (normalized * 24)
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(barHeight.dp)
                                .background(if (index == weekDays.lastIndex) PrimaryContainer else Color(0xFFE2E2E2), RoundedCornerShape(7.dp)),
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = day,
                            color = if (index == weekDays.lastIndex) Primary else Muted,
                            fontWeight = if (index == weekDays.lastIndex) FontWeight.Black else FontWeight.Medium,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }

            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(CardBorder))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Dushanba: ${formatWeight(weights.first())} kg", color = Ink, style = MaterialTheme.typography.labelMedium)
                Text(text = "Yakshanba: ${formatWeight(weights.last())} kg", color = Primary, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun MotivationCard() {
    Surface(
        shape = RoundedCornerShape(11.dp),
        color = Color(0xFFFFF9DF),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFFFE27A), RoundedCornerShape(11.dp)),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 22.dp, vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(17.dp),
        ) {
            Box(modifier = Modifier.size(43.dp).background(PrimaryContainer, CircleShape), contentAlignment = Alignment.Center) {
                Icon(imageVector = Icons.Outlined.WbSunny, contentDescription = null, tint = Primary, modifier = Modifier.size(23.dp))
            }
            Text(
                text = "Zo'r ketyapsiz! Har kuni kichik natija katta o'zgarishga olib keladi.",
                color = Primary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun WeeklyHistoryCard(history: List<WeightLog>) {
    val rows = history.takeLast(7).asReversed()
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(text = "Haftalik tarix", color = Ink, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium)
        ProgressSurface {
            Column(modifier = Modifier.fillMaxWidth()) {
                rows.forEachIndexed { index, item ->
                    HistoryRow(
                        day = displayDay(item.date, index),
                        weight = item.weightKg,
                        highlight = index == 0,
                        showDivider = index != rows.lastIndex,
                    )
                }
            }
        }
    }
}

@Composable
private fun HistoryRow(day: String, weight: Double, highlight: Boolean, showDivider: Boolean) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = day, color = Ink, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(
                    text = "${formatWeight(weight)} kg",
                    color = if (highlight) Primary else Ink,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (highlight) FontWeight.Bold else FontWeight.Normal,
                )
                if (highlight) Icon(imageVector = Icons.Outlined.TrendingDown, contentDescription = null, tint = Primary, modifier = Modifier.size(14.dp))
            }
        }
        if (showDivider) Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(CardBorder))
    }
}

@Composable
private fun InfoCard() {
    Surface(shape = RoundedCornerShape(10.dp), color = Color(0xFFF0F0F0), modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(horizontal = 22.dp, vertical = 22.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Icon(imageVector = Icons.Outlined.Info, contentDescription = null, tint = Muted, modifier = Modifier.size(22.dp))
            Text(
                text = "Har hafta bir vaqtda vazn o'lchash aniqroq natija beradi.",
                color = Muted,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun ProgressSurface(content: @Composable () -> Unit) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color.White,
        shadowElevation = 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CardBorder, RoundedCornerShape(10.dp)),
        content = content,
    )
}

private fun formatWeight(value: Double): String = String.format(Locale.US, "%.1f", value)

private fun displayDay(raw: String, index: Int): String {
    val fallback = listOf("Yakshanba", "Shanba", "Juma", "Payshanba", "Chorshanba", "Seshanba", "Dushanba")
    if (raw.isBlank()) return fallback.getOrElse(index) { raw }
    val lower = raw.lowercase(Locale.US)
    return when {
        lower.contains("sun") || lower.contains("yak") -> "Yakshanba"
        lower.contains("sat") || lower.contains("shan") -> "Shanba"
        lower.contains("fri") || lower.contains("jum") -> "Juma"
        lower.contains("thu") || lower.contains("pay") -> "Payshanba"
        lower.contains("wed") || lower.contains("chor") -> "Chorshanba"
        lower.contains("tue") || lower.contains("sesh") -> "Seshanba"
        lower.contains("mon") || lower.contains("dush") -> "Dushanba"
        else -> fallback.getOrElse(index) { raw }
    }
}
