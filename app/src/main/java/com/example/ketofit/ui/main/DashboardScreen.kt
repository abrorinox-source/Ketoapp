package com.example.ketofit.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.PlayCircleOutline
import androidx.compose.material.icons.outlined.RestaurantMenu
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ketofit.AppContainer
import com.example.ketofit.data.model.DashboardBundle
import com.example.ketofit.ui.theme.Primary
import com.example.ketofit.ui.theme.PrimaryContainer
import com.example.ketofit.ui.theme.SurfaceHighest
import com.example.ketofit.viewmodel.DashboardViewModel

private val Ink = Color(0xFF141414)
private val Muted = Color(0xFF5F5F5F)
private val SoftLine = Color(0xFFE6D8B9)
private val Olive = Color(0xFF6F6100)

@Composable
fun DashboardScreen(
    container: AppContainer,
    modifier: Modifier = Modifier,
    onNavigateToTab: ((Int) -> Unit)? = null,
) {
    val viewModel: DashboardViewModel = viewModel(factory = container.viewModelFactory)
    LaunchedEffect(Unit) { viewModel.loadDashboard() }

    val dashboard = viewModel.dashboard
    if (viewModel.isLoading && dashboard == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = PrimaryContainer)
        }
        return
    }

    val data = dashboard ?: return
    val startWeight = data.weightLogs.firstOrNull()?.weightKg ?: data.currentWeight
    val lostWeight = (startWeight - data.currentWeight).coerceAtLeast(0.0)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        HeaderBar()
        GreetingBlock(data = data)
        CourseHero(data = data)
        ActionGrid(onNavigateToTab = onNavigateToTab)
        WeightOverview(
            startWeight = startWeight,
            currentWeight = data.currentWeight,
            targetWeight = data.targetWeight,
            lostWeight = lostWeight,
        )
        ReminderCard()
        FeaturedMealCard()
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun HeaderBar() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(PrimaryContainer, CircleShape)
                    .border(2.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = "K", color = Primary, fontWeight = FontWeight.Black)
            }
            Text(text = "KETO CLARITY", color = Primary, fontWeight = FontWeight.Black, style = MaterialTheme.typography.titleLarge)
        }
        Surface(shape = CircleShape, color = Color.White, shadowElevation = 3.dp) {
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = "Notifications",
                tint = Primary,
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}

@Composable
private fun GreetingBlock(data: DashboardBundle) {
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Text(text = data.greeting, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Black, color = Ink)
        Text(text = data.subtitle, style = MaterialTheme.typography.bodyLarge, color = Color(0xFF202020))
    }
}

@Composable
private fun CourseHero(data: DashboardBundle) {
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = Color.White,
        shadowElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, SoftLine, RoundedCornerShape(22.dp)),
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Surface(shape = CircleShape, color = PrimaryContainer) {
                    Text(
                        text = "PREMIUM",
                        modifier = Modifier.padding(horizontal = 13.dp, vertical = 5.dp),
                        color = Primary,
                        fontWeight = FontWeight.Black,
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .background(
                            Brush.linearGradient(listOf(PrimaryContainer, Color(0xFFFFEA7A))),
                            CircleShape,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = "1", color = Primary, fontWeight = FontWeight.Black, style = MaterialTheme.typography.titleMedium)
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "30 kunlik keto kurs", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black, color = Ink)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "Progress", color = Ink, fontWeight = FontWeight.SemiBold)
                    Text(text = "${data.progressPercent}%", color = Ink, fontWeight = FontWeight.Bold)
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .background(Color(0xFFEDEDED), CircleShape),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth((data.progressPercent / 100f).coerceIn(0.02f, 1f))
                            .height(8.dp)
                            .background(Primary, CircleShape),
                    )
                }
            }

            Surface(shape = RoundedCornerShape(16.dp), color = Color(0xFFF6F6F6), modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.padding(15.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(Color.White, CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(imageVector = Icons.Outlined.PlayCircleOutline, contentDescription = null, tint = Primary)
                    }
                    Column {
                        Text(text = "Bugungi dars", color = Muted, style = MaterialTheme.typography.labelLarge)
                        Text(text = data.todayLessonTitle, color = Ink, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                    }
                }
            }
        }
    }
}

@Composable
private fun ActionGrid(onNavigateToTab: ((Int) -> Unit)?) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(14.dp), modifier = Modifier.fillMaxWidth()) {
            ActionTile("Bugungi dars", Icons.Outlined.School) { onNavigateToTab?.invoke(1) }
            ActionTile("Bugungi menyu", Icons.Outlined.RestaurantMenu) { onNavigateToTab?.invoke(2) }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(14.dp), modifier = Modifier.fillMaxWidth()) {
            ActionTile("Vazn kiritish", Icons.Outlined.PlayCircleOutline) { onNavigateToTab?.invoke(3) }
            ActionTile("Retseptlar", Icons.Outlined.MenuBook) { onNavigateToTab?.invoke(2) }
        }
    }
}

@Composable
private fun RowScope.ActionTile(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = Color.White,
        shadowElevation = 5.dp,
        modifier = Modifier
            .weight(1f)
            .height(154.dp)
            .border(1.dp, SoftLine, RoundedCornerShape(22.dp)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onClick)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(PrimaryContainer, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = Primary, modifier = Modifier.size(27.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = title, color = Ink, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
        }
    }
}

@Composable
private fun WeightOverview(
    startWeight: Double,
    currentWeight: Double,
    targetWeight: Double,
    lostWeight: Double,
) {
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = Color.White,
        shadowElevation = 6.dp,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, SoftLine, RoundedCornerShape(22.dp)),
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(PrimaryContainer.copy(alpha = 0.45f), CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = "kg", color = Primary, fontWeight = FontWeight.Black, style = MaterialTheme.typography.labelMedium)
                }
                Text(text = "Vazn o'zgarishi", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = Ink)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                WeightMetric("Boshlang'ich", startWeight)
                WeightMetric("Hozirgi", currentWeight, highlight = true)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                WeightMetric("Maqsad", targetWeight)
                WeightMetric("Kamaygan", lostWeight)
            }
        }
    }
}

@Composable
private fun RowScope.WeightMetric(title: String, value: Double, highlight: Boolean = false) {
    Column(
        modifier = Modifier
            .weight(1f)
            .border(1.dp, if (highlight) Primary.copy(alpha = 0.5f) else SurfaceHighest, RoundedCornerShape(14.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(text = title, color = Muted, style = MaterialTheme.typography.labelMedium)
        Text(
            text = "${value.toInt()} kg",
            color = if (highlight) Primary else Ink,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Black,
        )
    }
}

@Composable
private fun ReminderCard() {
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = Olive,
        shadowElevation = 5.dp,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(modifier = Modifier.size(42.dp).background(PrimaryContainer, CircleShape), contentAlignment = Alignment.Center) {
                Text(text = "!", color = Primary, fontWeight = FontWeight.Black)
            }
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(text = "ESLATMA", color = Color.White, fontWeight = FontWeight.Black, style = MaterialTheme.typography.labelLarge)
                Text(
                    text = "Bugun darsni ko'rib chiqing va menyuga amal qiling.",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@Composable
private fun FeaturedMealCard() {
    Surface(shape = RoundedCornerShape(22.dp), color = Color.White, shadowElevation = 7.dp, modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF315C39), Color(0xFFF1E7B7), Color(0xFFD0723D)),
                    ),
                ),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.38f)))),
            )
            Column(modifier = Modifier.align(Alignment.BottomStart).padding(18.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(text = "Bugungi tavsiya etilgan taom", color = Color.White, fontWeight = FontWeight.Black)
                Text(text = "Avokado, tuxum va losos", color = Color.White.copy(alpha = 0.86f), style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
