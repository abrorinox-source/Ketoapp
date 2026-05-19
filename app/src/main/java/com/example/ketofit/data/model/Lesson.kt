package com.example.ketofit.data.model

enum class LessonStatus { COMPLETED, OPEN, LOCKED }

data class Lesson(
    val id: String,
    val day: Int,
    val title: String,
    val description: String,
    val status: LessonStatus,
)

