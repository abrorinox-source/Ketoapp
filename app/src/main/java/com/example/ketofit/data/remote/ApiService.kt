package com.example.ketofit.data.remote

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

class ApiService {
    private var user: User? = null
    private var subscription: Subscription = Subscription(active = false, planName = "Free")
    private val lessons: MutableList<Lesson> = buildLessons()
    private val weightLogs: MutableList<WeightLog> = mutableListOf(
        WeightLog("2026-05-11", 88.0),
        WeightLog("2026-05-12", 87.4),
        WeightLog("2026-05-13", 86.9),
        WeightLog("2026-05-14", 86.2),
        WeightLog("2026-05-15", 85.8),
        WeightLog("2026-05-16", 85.4),
        WeightLog("2026-05-17", 84.8),
    )

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
        return ApiResponse(success = true, data = user ?: fallbackUser())
    }

    suspend fun updateProfile(profile: User): ApiResponse<User> {
        user = profile
        return ApiResponse(success = true, data = profile)
    }

    suspend fun getDashboard(): ApiResponse<DashboardBundle> {
        val currentUser = user ?: fallbackUser()
        val todayLesson = lessons.firstOrNull { it.status == LessonStatus.OPEN } ?: lessons.first()
        return ApiResponse(
            success = true,
            data = DashboardBundle(
                user = currentUser,
                subscription = subscription,
                todayLesson = todayLesson,
                todayMenu = todayMenu(),
                weightLogs = weightLogs.toList(),
            ),
        )
    }

    suspend fun getLessons(): ApiResponse<List<Lesson>> {
        return ApiResponse(success = true, data = lessons.toList())
    }

    suspend fun completeLesson(id: String): ApiResponse<Boolean> {
        val index = lessons.indexOfFirst { it.id == id }
        if (index == -1) return ApiResponse(success = false, message = "Lesson not found")
        lessons[index] = lessons[index].copy(status = LessonStatus.COMPLETED)
        if (index + 1 < lessons.size && lessons[index + 1].status == LessonStatus.LOCKED) {
            lessons[index + 1] = lessons[index + 1].copy(status = LessonStatus.OPEN)
        }
        return ApiResponse(success = true, data = true)
    }

    suspend fun getTodayMenu(): ApiResponse<Menu> {
        return ApiResponse(success = true, data = todayMenu())
    }

    suspend fun getProgress(): ApiResponse<ProgressData> {
        val currentUser = user ?: fallbackUser()
        return ApiResponse(
            success = true,
            data = ProgressData(
                startWeightKg = 88.0,
                currentWeightKg = currentUser.currentWeightKg,
                targetWeightKg = currentUser.targetWeightKg,
                history = weightLogs.toList(),
            ),
        )
    }

    suspend fun addWeight(weight: Double): ApiResponse<ProgressData> {
        weightLogs.add(WeightLog(date = "2026-05-18", weightKg = weight))
        val currentUser = (user ?: fallbackUser()).copy(currentWeightKg = weight)
        user = currentUser
        return getProgress()
    }

    fun currentSubscription(): Subscription = subscription

    private fun fallbackUser(): User {
        return User(
            id = "user_001",
            name = "Foydalanuvchi",
            email = "user@ketofit.app",
            language = "uz",
            gender = "belgilanmagan",
            age = 28,
            heightCm = 165.0,
            currentWeightKg = 84.8,
            targetWeightKg = 78.0,
            goal = "ozish",
            subscription = subscription,
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
