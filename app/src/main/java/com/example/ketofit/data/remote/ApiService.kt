package com.example.ketofit.data.remote

import com.example.ketofit.data.local.UserPreferences
import com.example.ketofit.data.model.AccessCode
import com.example.ketofit.data.model.ApiResponse
import com.example.ketofit.data.model.DashboardBundle
import com.example.ketofit.data.model.Lesson
import com.example.ketofit.data.model.LessonStatus
import com.example.ketofit.data.model.Meal
import com.example.ketofit.data.model.Menu
import com.example.ketofit.data.model.ProgressData
import com.example.ketofit.data.model.Subscription
import com.example.ketofit.data.model.User
import com.example.ketofit.data.model.WeightLog
import java.time.LocalDate

class ApiService(
    private val userPreferences: UserPreferences,
) {
    private var user: User? = null
    private var subscription: Subscription = Subscription(active = userPreferences.subscriptionActive, planName = if (userPreferences.subscriptionActive) "30 kunlik keto kurs" else "Free")
    private val lessons: MutableList<Lesson> = buildLessons()
    private val defaultWeightLogs: List<WeightLog> = listOf(
        WeightLog("2026-05-11", 88.0),
        WeightLog("2026-05-12", 87.4),
        WeightLog("2026-05-13", 86.9),
        WeightLog("2026-05-14", 86.2),
        WeightLog("2026-05-15", 85.8),
        WeightLog("2026-05-16", 85.4),
        WeightLog("2026-05-17", 84.8),
    )

    init {
        userPreferences.seedWeightLogsIfEmpty(defaultWeightLogs.map { com.example.ketofit.data.local.WeightPreferenceLog(it.date, it.weightKg) })
        applySavedLessonProgress()
    }

    suspend fun activateAccessCode(code: String): ApiResponse<AccessCode> {
        val normalized = code.trim()
        if (normalized.length < 6) {
            return ApiResponse(success = false, message = "Invalid access code")
        }
        subscription = Subscription(
            active = true,
            planName = "30 kunlik keto kurs",
            endsAt = "2026-06-16",
        )
        return ApiResponse(success = true, data = AccessCode(code = normalized, activated = true))
    }

    suspend fun saveProfile(profile: User): ApiResponse<User> {
        user = profile
        return ApiResponse(success = true, data = profile)
    }

    suspend fun getProfile(): ApiResponse<User> {
        return ApiResponse(success = true, data = currentUser())
    }

    suspend fun updateProfile(profile: User): ApiResponse<User> {
        user = profile
        userPreferences.saveProfile(
            name = profile.name,
            gender = profile.gender,
            age = profile.age,
            height = profile.heightCm,
            currentWeight = profile.currentWeightKg,
            targetWeight = profile.targetWeightKg,
            email = profile.email,
        )
        return ApiResponse(success = true, data = profile)
    }

    suspend fun getDashboard(): ApiResponse<DashboardBundle> {
        val currentUser = currentUser()
        val todayLesson = lessons.firstOrNull { it.status == LessonStatus.OPEN } ?: lessons.first()
        return ApiResponse(
            success = true,
            data = DashboardBundle(
                user = currentUser,
                subscription = currentSubscription(),
                todayLesson = todayLesson,
                todayMenu = todayMenu(),
                weightLogs = currentWeightLogs(),
            ),
        )
    }

    suspend fun getLessons(): ApiResponse<List<Lesson>> {
        applySavedLessonProgress()
        return ApiResponse(success = true, data = lessons.toList())
    }

    suspend fun completeLesson(id: String): ApiResponse<Boolean> {
        val index = lessons.indexOfFirst { it.id == id }
        if (index == -1) return ApiResponse(success = false, message = "Lesson not found")
        lessons[index] = lessons[index].copy(status = LessonStatus.COMPLETED)
        userPreferences.markLessonCompleted(id)
        if (index + 1 < lessons.size && lessons[index + 1].status == LessonStatus.LOCKED) {
            lessons[index + 1] = lessons[index + 1].copy(status = LessonStatus.OPEN)
        }
        return ApiResponse(success = true, data = true)
    }

    suspend fun getTodayMenu(): ApiResponse<Menu> {
        return ApiResponse(success = true, data = todayMenu())
    }

    suspend fun getProgress(): ApiResponse<ProgressData> {
        val currentUser = currentUser()
        return ApiResponse(
            success = true,
            data = ProgressData(
                startWeightKg = currentWeightLogs().firstOrNull()?.weightKg ?: currentUser.currentWeightKg,
                currentWeightKg = currentUser.currentWeightKg,
                targetWeightKg = currentUser.targetWeightKg,
                history = currentWeightLogs(),
            ),
        )
    }

    suspend fun addWeight(weight: Double): ApiResponse<ProgressData> {
        val today = LocalDate.now().toString()
        userPreferences.saveCurrentWeight(weight = weight, date = today)
        val currentUser = currentUser().copy(currentWeightKg = weight)
        user = currentUser
        return getProgress()
    }

    fun currentSubscription(): Subscription {
        return if (userPreferences.subscriptionActive) {
            subscription.copy(active = true, planName = if (subscription.planName == "Free") "30 kunlik keto kurs" else subscription.planName)
        } else {
            subscription.copy(active = false, planName = "Free")
        }
    }

    private fun currentUser(): User {
        user?.let { return it }
        val savedUser = savedUserOrNull()
        if (savedUser != null) {
            user = savedUser
            return savedUser
        }
        return fallbackUser()
    }

    private fun savedUserOrNull(): User? {
        if (!userPreferences.hasProfile) return null
        return User(
            id = userPreferences.userId ?: "user_001",
            name = userPreferences.name,
            email = userPreferences.email.ifBlank { "${userPreferences.name.trim().ifBlank { "user" }.lowercase().replace(" ", ".")}@gmail.com" },
            language = userPreferences.selectedLanguage,
            gender = userPreferences.gender,
            age = userPreferences.age,
            heightCm = userPreferences.height,
            currentWeightKg = userPreferences.currentWeight,
            targetWeightKg = userPreferences.targetWeight,
            goal = userPreferences.goal,
            subscription = currentSubscription(),
            reminderEnabled = true,
        )
    }

    private fun currentWeightLogs(): List<WeightLog> {
        val logs = userPreferences.weightLogs.map { WeightLog(date = it.date, weightKg = it.weightKg) }
        return logs.ifEmpty { defaultWeightLogs }
    }

    private fun applySavedLessonProgress() {
        val completed = userPreferences.completedLessonIds
        if (completed.isEmpty()) return
        lessons.forEachIndexed { index, lesson ->
            if (completed.contains(lesson.id)) {
                lessons[index] = lesson.copy(status = LessonStatus.COMPLETED)
                if (index + 1 < lessons.size && lessons[index + 1].status == LessonStatus.LOCKED) {
                    lessons[index + 1] = lessons[index + 1].copy(status = LessonStatus.OPEN)
                }
            }
        }
    }

    private fun fallbackUser(): User {
        return User(
            id = "user_001",
            name = "Foydalanuvchi",
            email = "user@ketofit.app",
            language = userPreferences.selectedLanguage,
            gender = "belgilanmagan",
            age = 28,
            heightCm = 165.0,
            currentWeightKg = 84.8,
            targetWeightKg = 78.0,
            goal = userPreferences.goal,
            subscription = currentSubscription(),
            reminderEnabled = true,
        )
    }

    private fun todayMenu(): Menu {
        return Menu(
            title = "Bugungi keto menyu",
            calories = 1600,
            protein = 110,
            fats = 120,
            carbs = 25,
            meals = listOf(
                Meal("Tuxumli salat va bodring", "Nonushta", 450, 25, 35, 5),
                Meal("Tovuqli karam salat", "Tushlik", 550, 40, 40, 8),
                Meal("Mol go'shti va ko'katlar", "Kechki ovqat", 500, 35, 35, 7),
                Meal("Pishloq va yong'oq", "Snack", 100, 10, 10, 5),
            ),
        )
    }

    private fun buildLessons(): MutableList<Lesson> {
        val firstLessons = listOf(
            "Keto nima?" to "Keto asoslari va organizm qanday ishlashi",
            "Nima yeyish mumkin?" to "Ruxsat etilgan mahsulotlar ro'yxati",
            "Nimalar mumkin emas?" to "Shakar, non, guruch va yashirin uglevodlar",
            "Uglevodni kamaytirish" to "Carb limit va almashtirish variantlari",
            "Protein va yog' balansi" to "To'yimli keto ovqatlanish formulasi",
            "Keto flu" to "Holsizlik, bosh og'rishi va elektrolitlar",
        )
        return MutableList(30) { index ->
            val day = index + 1
            val content = firstLessons.getOrNull(index) ?: ("Keto darsi" to "Keto kursining $day-kuni")
            Lesson(
                id = "lesson_$day",
                day = day,
                title = content.first,
                description = content.second,
                status = if (day == 1) LessonStatus.OPEN else LessonStatus.LOCKED,
            )
        }
    }
}