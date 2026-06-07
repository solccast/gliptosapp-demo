package com.example.gliptosapp.ui.settings

import android.content.Context
import android.util.TypedValue
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
fun Context.getThemeColor(attr: Int): Int {

    val typedValue = TypedValue()

    theme.resolveAttribute(
        attr,
        typedValue,
        true
    )

    return typedValue.data
}