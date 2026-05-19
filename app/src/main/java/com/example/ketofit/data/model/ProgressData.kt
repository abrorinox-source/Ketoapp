package com.example.ketofit.data.model

data class ProgressData(
    val startWeightKg: Double,
    val currentWeightKg: Double,
    val targetWeightKg: Double,
    val history: List<WeightLog>,
) {
    val lostKg: Double get() = (startWeightKg - currentWeightKg).coerceAtLeast(0.0)
    val remainingKg: Double get() = (currentWeightKg - targetWeightKg).coerceAtLeast(0.0)
    val lastWeekWeights: List<Double> get() = history.map { it.weightKg }
}

