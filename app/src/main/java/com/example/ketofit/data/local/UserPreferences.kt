package com.example.ketofit.data.local

import android.content.SharedPreferences

class UserPreferences(private val prefs: SharedPreferences) {
    companion object {
        private const val KEY_LANGUAGE = "selected_language"
        private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
        private const val KEY_SUBSCRIPTION_ACTIVE = "subscription_active"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_GOAL = "goal"
        private const val KEY_NAME = "name"
        private const val KEY_EMAIL = "email"
        private const val KEY_GENDER = "gender"
        private const val KEY_AGE = "age"
        private const val KEY_HEIGHT = "height"
        private const val KEY_CURRENT_WEIGHT = "current_weight"
        private const val KEY_TARGET_WEIGHT = "target_weight"
        private const val KEY_WEIGHT_LOGS = "weight_logs"
        private const val KEY_COMPLETED_LESSONS = "completed_lessons"
    }

    val selectedLanguage: String get() = prefs.getString(KEY_LANGUAGE, "uz") ?: "uz"
    val onboardingCompleted: Boolean get() = prefs.getBoolean(KEY_ONBOARDING_COMPLETED, false)
    val subscriptionActive: Boolean get() = prefs.getBoolean(KEY_SUBSCRIPTION_ACTIVE, false)
    val userId: String? get() = prefs.getString(KEY_USER_ID, null)
    val accessToken: String? get() = prefs.getString(KEY_ACCESS_TOKEN, null)
    val goal: String get() = prefs.getString(KEY_GOAL, "ozish") ?: "ozish"
    val name: String get() = prefs.getString(KEY_NAME, "") ?: ""
    val email: String get() = prefs.getString(KEY_EMAIL, "") ?: ""
    val gender: String get() = prefs.getString(KEY_GENDER, "male") ?: "male"
    val age: Int get() = prefs.getInt(KEY_AGE, 0)
    val height: Double get() = java.lang.Double.longBitsToDouble(prefs.getLong(KEY_HEIGHT, java.lang.Double.doubleToLongBits(0.0)))
    val currentWeight: Double get() = java.lang.Double.longBitsToDouble(prefs.getLong(KEY_CURRENT_WEIGHT, java.lang.Double.doubleToLongBits(0.0)))
    val targetWeight: Double get() = java.lang.Double.longBitsToDouble(prefs.getLong(KEY_TARGET_WEIGHT, java.lang.Double.doubleToLongBits(0.0)))

    val hasProfile: Boolean
        get() = name.isNotBlank() && age > 0 && height > 0.0 && currentWeight > 0.0 && targetWeight > 0.0

    fun setLanguage(language: String) {
        prefs.edit().putString(KEY_LANGUAGE, language).apply()
    }

    fun setOnboardingCompleted(value: Boolean) {
        prefs.edit().putBoolean(KEY_ONBOARDING_COMPLETED, value).apply()
    }

    fun setSubscriptionActive(value: Boolean) {
        prefs.edit().putBoolean(KEY_SUBSCRIPTION_ACTIVE, value).apply()
    }

    fun saveSession(userId: String, accessToken: String) {
        prefs.edit().putString(KEY_USER_ID, userId).putString(KEY_ACCESS_TOKEN, accessToken).apply()
    }

    fun saveProfile(
        name: String,
        gender: String,
        age: Int,
        height: Double,
        currentWeight: Double,
        targetWeight: Double,
        email: String = "${name.trim().ifBlank { "user" }.lowercase().replace(" ", ".")}@gmail.com",
    ) {
        prefs.edit()
            .putString(KEY_NAME, name)
            .putString(KEY_EMAIL, email)
            .putString(KEY_GENDER, gender)
            .putInt(KEY_AGE, age)
            .putLong(KEY_HEIGHT, java.lang.Double.doubleToLongBits(height))
            .putLong(KEY_CURRENT_WEIGHT, java.lang.Double.doubleToLongBits(currentWeight))
            .putLong(KEY_TARGET_WEIGHT, java.lang.Double.doubleToLongBits(targetWeight))
            .apply()
    }

    fun saveGoal(goal: String) {
        prefs.edit().putString(KEY_GOAL, goal).apply()
    }

    fun saveCurrentWeight(weight: Double, date: String) {
        val updatedLogs = (weightLogs + WeightPreferenceLog(date = date, weightKg = weight))
            .takeLast(30)
            .joinToString(separator = "|") { "${it.date},${it.weightKg}" }

        prefs.edit()
            .putLong(KEY_CURRENT_WEIGHT, java.lang.Double.doubleToLongBits(weight))
            .putString(KEY_WEIGHT_LOGS, updatedLogs)
            .apply()
    }

    fun seedWeightLogsIfEmpty(logs: List<WeightPreferenceLog>) {
        if (prefs.getString(KEY_WEIGHT_LOGS, null).isNullOrBlank()) {
            prefs.edit()
                .putString(KEY_WEIGHT_LOGS, logs.joinToString(separator = "|") { "${it.date},${it.weightKg}" })
                .apply()
        }
    }

    val weightLogs: List<WeightPreferenceLog>
        get() = prefs.getString(KEY_WEIGHT_LOGS, "")
            .orEmpty()
            .split("|")
            .mapNotNull { raw ->
                val parts = raw.split(",")
                val date = parts.getOrNull(0).orEmpty()
                val weight = parts.getOrNull(1)?.toDoubleOrNull()
                if (date.isBlank() || weight == null) null else WeightPreferenceLog(date, weight)
            }

    fun markLessonCompleted(id: String) {
        val updated = (completedLessonIds + id).toSet().joinToString(",")
        prefs.edit().putString(KEY_COMPLETED_LESSONS, updated).apply()
    }

    val completedLessonIds: Set<String>
        get() = prefs.getString(KEY_COMPLETED_LESSONS, "")
            .orEmpty()
            .split(",")
            .filter { it.isNotBlank() }
            .toSet()

    fun clearSession() {
        prefs.edit()
            .remove(KEY_USER_ID)
            .remove(KEY_ACCESS_TOKEN)
            .putBoolean(KEY_ONBOARDING_COMPLETED, false)
            .putBoolean(KEY_SUBSCRIPTION_ACTIVE, false)
            .apply()
    }
}

data class WeightPreferenceLog(
    val date: String,
    val weightKg: Double,
)
