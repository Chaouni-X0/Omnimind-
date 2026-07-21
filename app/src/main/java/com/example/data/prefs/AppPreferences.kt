package com.example.omnimind.data.prefs

import android.content.Context
import android.content.SharedPreferences

/**
 * تفضيلات خفيفة لا تحتاج قاعدة بيانات: السمة المختارة وحالة إتمام الإعداد الأولي.
 */
class AppPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences("omnimind_prefs", Context.MODE_PRIVATE)

    var isOnboardingComplete: Boolean
        get() = prefs.getBoolean(KEY_ONBOARDING_DONE, false)
        set(value) = prefs.edit().putBoolean(KEY_ONBOARDING_DONE, value).apply()

    var selectedTheme: String
        get() = prefs.getString(KEY_THEME, AppTheme.OBSIDIAN.name) ?: AppTheme.OBSIDIAN.name
        set(value) = prefs.edit().putString(KEY_THEME, value).apply()

    companion object {
        private const val KEY_ONBOARDING_DONE = "onboarding_complete"
        private const val KEY_THEME = "selected_theme"
    }
}

enum class AppTheme {
    OBSIDIAN, // داكن بنفسجي
    AURORA,   // فاتح تركواز
    EMBER     // داكن برتقالي
}
