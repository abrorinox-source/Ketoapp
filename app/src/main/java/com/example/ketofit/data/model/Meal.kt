package com.example.ketofit.data.model

data class Meal(
    val title: String,
    val time: String,
    val calories: Int,
    val protein: Int,
    val fats: Int,
    val carbs: Int,
    val items: List<String> = emptyList(),
)

