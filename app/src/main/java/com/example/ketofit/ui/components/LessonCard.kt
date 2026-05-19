package com.example.ketofit.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ketofit.data.model.Lesson
import com.example.ketofit.data.model.LessonStatus

@Composable
fun LessonCard(
    lesson: Lesson,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AppCard(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Kun ${lesson.day}", fontWeight = FontWeight.SemiBold)
                AssistChip(onClick = onClick, label = {
                    Text(
                        text = when (lesson.status) {
                            LessonStatus.COMPLETED -> "Completed"
                            LessonStatus.OPEN -> "Open"
                            LessonStatus.LOCKED -> "Locked"
                        }
                    )
                })
            }
            Text(text = lesson.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(text = lesson.description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
