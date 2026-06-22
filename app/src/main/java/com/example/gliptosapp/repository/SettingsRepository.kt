package com.example.gliptosapp.repository

import android.content.Context
import com.example.gliptosapp.ui.settings.ContrastPreferences
import com.example.gliptosapp.ui.settings.FontPreferences
import com.example.gliptosapp.ui.settings.FontScale
import com.example.gliptosapp.ui.settings.InteractionMode
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import androidx.core.content.edit

class SettingsRepository @Inject constructor(
    @ApplicationContext
    private val context: Context
) {

    private val prefs by lazy {
        context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    }

    fun getFontScale(): FontScale {
        return FontPreferences.get(context)
    }

    fun saveFontScale(scale: FontScale) {
        FontPreferences.save(context, scale)
    }

    fun isHighContrastEnabled(): Boolean {
        return ContrastPreferences.isEnabled(context)
    }

    fun saveHighContrast(enabled: Boolean) {
        ContrastPreferences.save(context, enabled)
    }
    fun getInteractionMode(): InteractionMode {

        val value = prefs.getString("interaction_mode", InteractionMode.PIECE_FIRST.name)

        return InteractionMode.valueOf(value!!)
    }

    fun saveInteractionMode(mode: InteractionMode) {
        prefs.edit { putString("interaction_mode", mode.name) }
    }

    fun isSoundEnabled(): Boolean {
        return prefs.getBoolean("sound_enabled", true)
    }

    fun saveSoundEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("sound_enabled", enabled).apply()
    }
}