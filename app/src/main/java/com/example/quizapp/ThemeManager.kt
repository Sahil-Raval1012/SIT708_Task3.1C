package com.example.quizapp

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

object ThemeManager {

    private const val PREFS = "quiz_prefs"
    private const val KEY = "dark_mode"

    fun isDark(context: Context): Boolean =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getBoolean(KEY, false)

    fun setDark(context: Context, dark: Boolean) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().putBoolean(KEY, dark).apply()
    }
    fun applyFromPrefs(context: Context) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDark(context)) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}
