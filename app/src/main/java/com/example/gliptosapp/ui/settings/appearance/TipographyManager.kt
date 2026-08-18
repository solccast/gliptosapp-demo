package com.example.gliptosapp.ui.settings.appearance

import android.util.TypedValue
import android.widget.TextView

object TypographyManager {

    private const val DYSLEXIA_SIZE_FACTOR = 0.82f
    fun apply(
        textView: TextView,
        baseSizeSp: Float
    ) {

        val scale = FontPreferences.get(textView.context)

        val adjustedBaseSize =
            if (FontPreferences.getFamily(textView.context) == FontFamily.DYSLEXIA)
                baseSizeSp * DYSLEXIA_SIZE_FACTOR
            else
                baseSizeSp

        textView.setTextSize(
            TypedValue.COMPLEX_UNIT_SP,
            adjustedBaseSize * scale.multiplier
        )
    }
}