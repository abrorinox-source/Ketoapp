package com.example.ketofit.data.repository

import com.example.ketofit.data.model.Lesson
import com.example.ketofit.data.remote.ApiService

class LessonRepository(
    private val apiService: ApiService,
) {
    suspend fun getLessons(): List<Lesson> {
        val response = apiService.getLessons()
        if (!response.success || response.data == null) {
            throw IllegalStateException(response.message ?: "Load lessons failed")
        }
        return response.data
    }

    suspend fun completeLesson(id: String) {
        val response = apiService.completeLesson(id)
        if (!response.success) {
            throw IllegalStateException(response.message ?: "Complete lesson failed")
        }
    }
}

