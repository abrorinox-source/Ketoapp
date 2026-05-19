package com.example.ketofit.data.model

data class Menu(
    val title: String,
    val calories: Int,
    val protein: Int,
    val fats: Int,
    val carbs: Int,
    val meals: List<Meal>,
)

