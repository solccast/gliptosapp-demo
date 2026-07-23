package com.example.gliptosapp.ui.settings.appearance

import android.util.TypedValue
import android.widget.TextView

object TypographyManager {

    fun apply(
        textView: TextView,
        baseSizeSp: Float
    ) {

        val multiplier = FontPreferences
            .get(textView.context)
            .multiplier

        textView.setTextSize(
            TypedValue.COMPLEX_UNIT_SP,
            baseSizeSp * multiplier
        )
    }
}