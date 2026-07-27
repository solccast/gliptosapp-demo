package com.example.gliptosapp.ui.settings.appearance

import android.util.TypedValue
import android.widget.TextView

object TypographyManager {

    fun apply(
        textView: TextView,
        baseSizeSp: Float
    ) {

        val scale =
            FontPreferences
                .get(textView.context)

        val multiplier =
            effectiveMultiplier(
                scale.multiplier,
                baseSizeSp
            )

        textView.setTextSize(
            TypedValue.COMPLEX_UNIT_SP,
            baseSizeSp * multiplier
        )
    }

    private fun effectiveMultiplier(
        multiplier: Float,
        size: Float
    ): Float {

        return when {

            multiplier <= 1f ->
                multiplier

            size >= 36f ->
                1f + (multiplier - 1f) * 0.45f

            size >= 28f ->
                1f + (multiplier - 1f) * 0.65f

            size >= 20f ->
                1f + (multiplier - 1f) * 0.85f

            else ->
                multiplier
        }
    }
}