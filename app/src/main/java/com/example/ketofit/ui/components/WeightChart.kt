package com.example.ketofit.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun WeightChart(
    weights: List<Double>,
    modifier: Modifier = Modifier,
) {
    val lineColor = MaterialTheme.colorScheme.primary
    Column(modifier = modifier) {
        Text(text = "7 kunlik grafik", style = MaterialTheme.typography.titleMedium)
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
        ) {
            if (weights.isEmpty()) return@Canvas
            val max = weights.maxOrNull() ?: 1.0
            val min = weights.minOrNull() ?: 0.0
            val range = (max - min).takeIf { it > 0 } ?: 1.0
            val stepX = size.width / (weights.size.coerceAtLeast(2) - 1)
            val points = weights.mapIndexed { index, weight ->
                val x = index * stepX
                val normalized = ((weight - min) / range).toFloat()
                val y = size.height - (normalized * (size.height - 20f)) - 10f
                Offset(x, y)
            }
            points.forEach { point ->
                drawCircle(color = lineColor, radius = 6f, center = point)
            }
            if (points.size > 1) {
                val path = Path().apply {
                    moveTo(points.first().x, points.first().y)
                    points.drop(1).forEach { lineTo(it.x, it.y) }
                }
                drawPath(path = path, color = lineColor, style = Stroke(width = 4f))
            }
        }
    }
}
