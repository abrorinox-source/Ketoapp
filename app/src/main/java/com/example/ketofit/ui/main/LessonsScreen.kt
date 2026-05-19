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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ketofit.AppContainer
import com.example.ketofit.data.model.Lesson
import com.example.ketofit.data.model.LessonStatus
import com.example.ketofit.viewmodel.LessonsViewModel

private val LessonGreen = Color(0xFF22C55E)
private val LessonGreenSoft = Color(0xFFB8F7D0)
private val LessonText = Color(0xFF181A1A)
private val LessonMuted = Color(0xFF6B6B6B)

@Composable
fun LessonsScreen(
    container: AppContainer,
    modifier: Modifier = Modifier,
) {
    val viewModel: LessonsViewModel = viewModel(factory = container.viewModelFactory)
    LaunchedEffect(Unit) { viewModel.loadLessons() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        LessonsHeader()
        TitleBlock()
        CourseProgressCard()

        if (viewModel.isLoading && viewModel.lessons.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = LessonGreen)
            }
        } else {
            viewModel.lessons.take(6).forEach { lesson ->
                LessonRow(
                    lesson = lesson,
                    onClick = {
                        if (lesson.status == LessonStatus.OPEN) {
                            viewModel.completeLesson(lesson.id)
                        }
                    },
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        BottomCta()
    }
}

@Composable
private fun LessonsHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(Color(0xFFF2F4F3), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = "U", color = LessonGreen, fontWeight = FontWeight.Bold)
            }
            Text(text = "KETO CLARITY", color = LessonGreen, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
        }
        Icon(imageVector = Icons.Outlined.Notifications, contentDescription = "Notifications", tint = Color(0xFF111111))
    }
}

@Composable
private fun TitleBlock() {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(text = "Darslar", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = LessonText)
        Text(
            text = "30 kunlik keto kurs — darslar kunma-kun ochiladi.",
            style = MaterialTheme.typography.bodyLarge,
            color = LessonText,
        )
    }
}

@Composable
private fun CourseProgressCard() {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = LessonGreenSoft,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "30 kunlik keto kurs", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color(0xFF064E3B))
                Box(
                    modifier = Modifier
                        .background(Color(0xFF064E3B), RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                ) {
                    Text(text = "PREMIUM", color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "1-kun", color = Color(0xFF064E3B), fontWeight = FontWeight.SemiBold)
                Text(text = "1/30 dars ochildi", color = Color(0xFF064E3B), fontWeight = FontWeight.SemiBold)
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(7.dp)
                    .background(Color.White.copy(alpha = 0.45f), CircleShape),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.033f)
                        .height(7.dp)
                        .background(Color(0xFF064E3B), CircleShape),
                )
            }
        }
    }
}

@Composable
private fun LessonRow(
    lesson: Lesson,
    onClick: () -> Unit,
) {
    val isOpen = lesson.status == LessonStatus.OPEN
    val container = if (isOpen) Color.White else Color(0xFFF5F5F5)
    val border = if (isOpen) LessonGreen else Color.Transparent
    val alpha = if (isOpen) 1f else 0.62f

    Surface(
        shape = RoundedCornerShape(18.dp),
        color = container,
        modifier = Modifier
            .fillMaxWidth()
            .border(if (isOpen) 2.dp else 0.dp, border, RoundedCornerShape(18.dp))
            .clickable(enabled = isOpen, onClick = onClick),
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .alpha(alpha),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(if (isOpen) Color(0xFFBBF7D0) else Color(0xFFECECEC), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = if (isOpen) Icons.Outlined.PlayCircle else Icons.Outlined.Lock,
                    contentDescription = null,
                    tint = if (isOpen) LessonGreen else Color(0xFF767676),
                )
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(text = "${lesson.day}-kun: ${lesson.title}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = LessonText)
                Text(text = lesson.description, style = MaterialTheme.typography.bodyMedium, color = LessonMuted)
                if (isOpen) {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFE8FBEF), CircleShape)
                            .padding(horizontal = 14.dp, vertical = 5.dp),
                    ) {
                        Text(text = "Ochiq", color = LessonGreen, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.labelMedium)
                    }
                } else {
                    Text(
                        text = when (lesson.day) {
                            2 -> "Ertaga ochiladi"
                            3 -> "2 kundan keyin"
                            else -> ""
                        },
                        color = LessonMuted,
                        fontStyle = FontStyle.Italic,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
            if (isOpen) {
                Icon(imageVector = Icons.Outlined.ChevronRight, contentDescription = null, tint = LessonGreen)
            }
        }
    }
}

@Composable
private fun BottomCta() {
    Surface(
        shape = RoundedCornerShape(28.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFD9DDE1), RoundedCornerShape(28.dp)),
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(22.dp),
        ) {
            Text(
                text = "Har kuni yangi dars ochiladi. Kursni tartib bilan davom ettiring.",
                style = MaterialTheme.typography.bodyLarge,
                color = LessonText,
            )
            Surface(
                shape = CircleShape,
                color = LessonGreen,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Box(modifier = Modifier.padding(vertical = 16.dp), contentAlignment = Alignment.Center) {
                    Text(text = "BUGUNGI DARSNI BOSHLASH", color = Color.White, fontWeight = FontWeight.Bold, letterSpacing = 3.sp)
                }
            }
        }
    }
}
