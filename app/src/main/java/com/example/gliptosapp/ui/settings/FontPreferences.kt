package com.example.gliptosapp.ui.settings

import android.content.Context
import androidx.core.content.edit

enum class FontScale(
    val displayName: String,
    val multiplier: Float
) {
    SMALL(
        displayName = "pequeño",
        multiplier = 0.85f
    ),

    MEDIUM(
        displayName = "mediano",
        multiplier = 1.0f
    ),

    LARGE(
        displayName = "grande",
        multiplier = 1.25f
    )
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