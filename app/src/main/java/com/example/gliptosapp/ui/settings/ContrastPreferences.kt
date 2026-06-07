package com.example.gliptosapp.ui.settings

import android.content.Context
import androidx.core.content.edit

object ContrastPreferences {

    private const val PREF_NAME = "accessibility_settings"
    private const val KEY = "high_contrast"

    fun save(
        context: Context,
        enabled: Boolean
    ) {
        context
            .getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )
            .edit {
                putBoolean(KEY, enabled)
            }
    }

    fun isEnabled(
        context: Context
    ): Boolean {

        return context
            .getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )
            .getBoolean(KEY, false)
    }
}