package com.example.ketofit.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ketofit.data.model.Meal

@Composable
fun MealCard(
    meal: Meal,
    modifier: Modifier = Modifier,
) {
    AppCard(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(text = meal.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                    Text(text = meal.time, style = MaterialTheme.typography.bodySmall)
                }
                Text(text = "${meal.calories} kcal")
            }
            Text(text = "P ${meal.protein}g  F ${meal.fats}g  C ${meal.carbs}g")
            if (meal.items.isNotEmpty()) {
                Text(text = meal.items.joinToString(" • "), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
