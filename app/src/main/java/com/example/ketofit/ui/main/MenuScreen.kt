package com.example.ketofit.ui.main

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocalDining
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.RestaurantMenu
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ketofit.AppContainer
import com.example.ketofit.data.model.Meal
import com.example.ketofit.data.model.Menu
import com.example.ketofit.ui.theme.Primary
import com.example.ketofit.ui.theme.PrimaryContainer
import com.example.ketofit.viewmodel.MenuViewModel

private val MenuInk = Color(0xFF141414)
private val MenuMuted = Color(0xFF5E5E5E)
private val MenuLine = Color(0xFFE7DFCE)
private val MenuOlive = Color(0xFF756200)
private val MenuRed = Color(0xFFC41717)

@Composable
fun MenuScreen(
    container: AppContainer,
    modifier: Modifier = Modifier,
) {
    val viewModel: MenuViewModel = viewModel(factory = container.viewModelFactory)
    LaunchedEffect(Unit) { viewModel.loadTodayMenu() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        MenuHeader()
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = "Bugungi menyu", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black, color = MenuInk)
            Text(text = "Keto uchun mos kunlik ovqatlanish rejasi.", style = MaterialTheme.typography.bodyMedium, color = MenuInk)
        }

        if (viewModel.isLoading && viewModel.menu == null) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryContainer)
            }
        }

        viewModel.menu?.let { menu ->
            SummaryCard(menu)
            MacroDashboard(menu)
            menu.meals.forEachIndexed { index, meal ->
                MealCardV2(meal = meal, index = index)
            }
            HydrationCard()
            RecipeCta()
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun MenuHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .background(PrimaryContainer, CircleShape)
                    .border(2.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = "K", color = Primary, fontWeight = FontWeight.Black)
            }
            Text(text = "KETO CLARITY", color = Color(0xFF21C45A), fontWeight = FontWeight.Black, style = MaterialTheme.typography.titleLarge)
        }
        Surface(shape = CircleShape, color = Color.White, shadowElevation = 3.dp) {
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = "Notifications",
                tint = MenuInk,
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}

@Composable
private fun SummaryCard(menu: Menu) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 6.dp,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Box(
            modifier = Modifier
                .background(Brush.linearGradient(listOf(Color.White, Color(0xFFFFF7DA))))
                .border(1.dp, MenuLine, RoundedCornerShape(20.dp))
                .padding(18.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "1-KUN MENYUSI", color = MenuOlive, fontWeight = FontWeight.Black, style = MaterialTheme.typography.labelMedium)
                    Surface(shape = CircleShape, color = Color.White, shadowElevation = 2.dp) {
                        Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "Premium", color = MenuOlive, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
                Text(
                    text = "Uglevodni\nkamaytirish",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MenuInk,
                )
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(text = "${menu.calories}", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Light, color = Color.Black)
                    Text(text = "kcal", modifier = Modifier.padding(start = 8.dp, bottom = 7.dp), color = MenuInk)
                    Spacer(modifier = Modifier.weight(1f))
                    Surface(shape = CircleShape, color = Color.White, shadowElevation = 3.dp) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                        ) {
                            Icon(imageVector = Icons.Outlined.RestaurantMenu, contentDescription = null, tint = MenuOlive, modifier = Modifier.size(15.dp))
                            Text(text = "${menu.carbs}g carb max", style = MaterialTheme.typography.labelSmall, color = MenuInk)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MacroDashboard(menu: Menu) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        MacroGauge("75%", "${menu.protein}g", "PROTEIN", 0.75f, Color(0xFF6D6D6D), active = false)
        MacroGauge("80%", "${menu.fats}g", "YOG'", 0.80f, MenuOlive, active = true)
        MacroGauge("25%", "${menu.carbs}g", "CARB", 0.25f, MenuRed, active = false)
    }
}

@Composable
private fun RowScope.MacroGauge(
    percentText: String,
    value: String,
    label: String,
    progress: Float,
    color: Color,
    active: Boolean,
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = if (active) Color(0xFFFFF9D9) else Color.White,
        shadowElevation = 7.dp,
        modifier = Modifier
            .weight(1f)
            .height(104.dp),
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Box(modifier = Modifier.size(38.dp), contentAlignment = Alignment.Center) {
                GaugeArc(progress = progress, color = color)
                Text(text = percentText, style = MaterialTheme.typography.labelSmall, color = MenuInk)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = value, style = MaterialTheme.typography.titleMedium, color = MenuInk)
                Text(text = label, style = MaterialTheme.typography.labelSmall, color = if (active) MenuOlive else MenuInk)
            }
        }
    }
}

@Composable
private fun GaugeArc(progress: Float, color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val stroke = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
        drawArc(
            color = Color(0xFFE8E8E8),
            startAngle = -90f,
            sweepAngle = 360f,
            useCenter = false,
            style = stroke,
        )
        drawArc(
            color = color,
            startAngle = -90f,
            sweepAngle = 360f * progress,
            useCenter = false,
            style = stroke,
        )
    }
}

@Composable
private fun MealCardV2(meal: Meal, index: Int) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        shadowElevation = 7.dp,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp)),
            ) {
                FoodHero(index = index)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.50f)))),
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    Text(text = meal.time.uppercase(), color = PrimaryContainer, fontWeight = FontWeight.Black, style = MaterialTheme.typography.labelSmall)
                    Text(text = meal.title, color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                }
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 3.dp,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(14.dp),
                ) {
                    Text(
                        text = "${meal.calories} kcal",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MenuInk,
                    )
                }
            }

            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    MacroDot("Oqsil: ${meal.protein}g", Color(0xFF505050))
                    MacroDot("Yog': ${meal.fats}g", MenuOlive)
                    MacroDot("Uglevod: ${meal.carbs}g", MenuRed)
                }
                Surface(shape = RoundedCornerShape(12.dp), color = Color(0xFFFAFAFA), modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .border(1.dp, Color(0xFFE4E4E4), RoundedCornerShape(12.dp))
                            .padding(vertical = 11.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(imageVector = Icons.Outlined.SwapHoriz, contentDescription = null, tint = MenuOlive, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.size(6.dp))
                        Text(text = "Almashtirish", color = MenuOlive, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
private fun FoodHero(index: Int) {
    val colors = when (index) {
        0 -> listOf(Color(0xFF1F7C3B), Color(0xFFE63525), Color(0xFFFFD13B), Color(0xFF81B34A))
        1 -> listOf(Color(0xFFEDE6D4), Color(0xFF7A9F54), Color(0xFFB7793E), Color(0xFFF6F2EA))
        2 -> listOf(Color(0xFF0F1514), Color(0xFFC6544A), Color(0xFF5F9253), Color(0xFF1B241B))
        else -> listOf(Color(0xFFE6B34A), Color(0xFFF5D27C), Color(0xFF8A5727), Color(0xFFF5E2BB))
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(colors)),
    )
}

@Composable
private fun MacroDot(text: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        Box(modifier = Modifier.size(6.dp).background(color, CircleShape))
        Text(text = text, style = MaterialTheme.typography.labelSmall, color = MenuInk)
    }
}

@Composable
private fun HydrationCard() {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        shadowElevation = 7.dp,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(Color(0xFFFFFAE8), CircleShape)
                    .border(1.dp, MenuLine, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(imageVector = Icons.Outlined.WaterDrop, contentDescription = null, tint = MenuOlive)
            }
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(text = "Gidratatsiya", color = MenuInk, style = MaterialTheme.typography.titleSmall)
                Text(text = "Bugun kamida 2 litr suv ichishni unutmang.", color = MenuInk, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun RecipeCta() {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MenuOlive,
        shadowElevation = 5.dp,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(vertical = 15.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(imageVector = Icons.Outlined.MenuBook, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = "To'liq retseptlarni ko'rish", color = Color.White, fontWeight = FontWeight.SemiBold)
        }
    }
}
