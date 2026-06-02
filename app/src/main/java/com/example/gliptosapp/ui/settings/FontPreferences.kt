package com.example.gliptosapp.ui.settings

import android.content.Context
import androidx.core.content.edit

enum class FontScale(val multiplier: Float) {
    SMALL(0.9f),
    MEDIUM(1.0f),
    LARGE(1.2f)
}

object FontPreferences {

    private const val PREF_NAME = "accessibility_settings"
    private const val FONT_KEY = "font_size"

    fun save(
        context: Context,
        scale: FontScale
    ) {
        context
            .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit { putString(FONT_KEY, scale.name) }
    }

    fun get(context: Context): FontScale {

        val value = context
            .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(FONT_KEY, FontScale.MEDIUM.name)

        return FontScale.valueOf(value!!)
    }
}