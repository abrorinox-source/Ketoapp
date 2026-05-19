package com.example.ketofit.data.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val language: String,
    val gender: String,
    val age: Int,
    val heightCm: Double,
    val currentWeightKg: Double,
    val targetWeightKg: Double,
    val goal: String,
    val subscription: Subscription,
    val reminderEnabled: Boolean,
)

