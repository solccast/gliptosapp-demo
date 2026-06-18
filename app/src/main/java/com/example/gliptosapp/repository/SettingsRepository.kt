package com.example.gliptosapp.repository

import android.content.Context
import com.example.gliptosapp.ui.settings.ContrastPreferences
import com.example.gliptosapp.ui.settings.FontPreferences
import com.example.gliptosapp.ui.settings.FontScale
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SettingsRepository @Inject constructor(
    @ApplicationContext
    private val context: Context
) {

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
}