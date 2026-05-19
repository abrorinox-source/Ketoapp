package com.example.ketofit.data.model

data class DashboardBundle(
    val user: User,
    val subscription: Subscription,
    val todayLesson: Lesson,
    val todayMenu: Menu,
    val weightLogs: List<WeightLog>,
) {
    val greeting: String get() = "Salom, ${user.name.split(' ').firstOrNull().orEmpty()}"
    val subtitle: String get() = "Bugun keto kursining ${todayLesson.day}-kuni"
    val progressPercent: Int get() = ((todayLesson.day / 30f) * 100).toInt()
    val todayLessonTitle: String get() = todayLesson.title
    val todayMenuTitle: String get() = todayMenu.title
    val currentWeight: Double get() = user.currentWeightKg
    val targetWeight: Double get() = user.targetWeightKg
    val subscriptionLabel: String get() = if (subscription.active) "Premium active" else "Free plan"
}

