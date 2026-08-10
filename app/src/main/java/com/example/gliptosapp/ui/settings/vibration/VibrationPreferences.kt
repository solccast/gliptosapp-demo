package com.example.gliptosapp.ui.settings.vibration

import android.content.Context
import androidx.core.content.edit

object VibrationPreferences {

    private const val PREF_NAME = "settings"
    private const val KEY_VIBRATION = "vibration_enabled"

    fun isEnabled(context: Context): Boolean {
        return context
            .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_VIBRATION, true)
    }

    fun save(context: Context, enabled: Boolean) {
        context
            .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit {
                putBoolean(KEY_VIBRATION, enabled)
            }
    }
}