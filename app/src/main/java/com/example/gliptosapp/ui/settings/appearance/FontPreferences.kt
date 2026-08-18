package com.example.gliptosapp.ui.settings.appearance

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
    private const val FONT_SCALE_KEY = "font_scale"
    private const val FONT_FAMILY_KEY = "font_family"

    fun save(
        context: Context,
        scale: FontScale
    ) {
        context
            .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit { putString(FONT_SCALE_KEY, scale.name) }
    }

    fun get(context: Context): FontScale {

        val value = context
            .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(FONT_SCALE_KEY, FontScale.MEDIUM.name)

        return FontScale.valueOf(value!!)
    }

    fun saveFamily(
        context: Context,
        family: FontFamily
    ) {

        context
            .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit {
                putString(
                    FONT_FAMILY_KEY,
                    family.name
                )
            }
    }
    fun getFamily(
        context: Context
    ): FontFamily {

        val value =
            context
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getString(
                    FONT_FAMILY_KEY,
                    FontFamily.DEFAULT.name
                )!!

        return FontFamily.valueOf(value)
    }
}