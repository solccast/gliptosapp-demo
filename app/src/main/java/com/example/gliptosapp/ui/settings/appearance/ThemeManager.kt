package com.example.gliptosapp.ui.settings.appearance

import android.content.Context
import androidx.annotation.FontRes
import com.example.gliptosapp.R

object ThemeManager {

    fun getTheme(
        context: Context
    ): Int {

        val highContrast =
            ContrastPreferences.isEnabled(context)

        val dyslexia =
            FontPreferences.getFamily(context) == FontFamily.DYSLEXIA

        return when {

            highContrast && dyslexia ->
                R.style.Theme_GliptosApp_HighContrast_Dyslexia

            highContrast ->
                R.style.Theme_GliptosApp_HighContrast

            dyslexia ->
                R.style.Theme_GliptosApp_Dyslexia

            else ->
                R.style.Theme_GliptosApp
        }
    }
}