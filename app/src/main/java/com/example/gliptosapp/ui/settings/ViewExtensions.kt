package com.example.gliptosapp.ui.settings

import android.view.ViewGroup
import android.widget.TextView

fun ViewGroup.applyFontScale() {

    for (i in 0 until childCount) {

        val child = getChildAt(i)

        when (child) {

            is TextView -> {

                val originalSize =
                    child.textSize /
                            child.resources.displayMetrics.scaledDensity

                TypographyManager.apply(
                    child,
                    originalSize
                )
            }

            is ViewGroup -> {
                child.applyFontScale()
            }
        }
    }
}