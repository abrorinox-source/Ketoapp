package com.example.ketofit.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ketofit.data.model.Lesson
import com.example.ketofit.data.repository.LessonRepository
import kotlinx.coroutines.launch

class LessonsViewModel(
    private val lessonRepository: LessonRepository,
) : ViewModel() {
    var isLoading by mutableStateOf(false)
        private set
    var lessons by mutableStateOf<List<Lesson>>(emptyList())
        private set

    fun loadLessons() {
        isLoading = true
        viewModelScope.launch {
            lessons = lessonRepository.getLessons()
            isLoading = false
        }
    }

    fun completeLesson(id: String) {
        viewModelScope.launch {
            lessonRepository.completeLesson(id)
            loadLessons()
        }
    }
}

