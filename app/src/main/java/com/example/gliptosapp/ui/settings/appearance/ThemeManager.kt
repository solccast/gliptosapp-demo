package com.example.gliptosapp.ui.settings.appearance

import android.content.Context
import com.example.gliptosapp.R
/**
* Recurso de tema actual
* */
object ThemeManager {

    fun getTheme(
        context: Context
    ): Int {

        return if (
            ContrastPreferences.isEnabled(context)
        ) {
            R.style.Theme_GliptosApp_HighContrast
        } else {
            R.style.Theme_GliptosApp
        }
    }
}