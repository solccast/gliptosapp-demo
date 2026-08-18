package com.example.gliptosapp.ui.settings.appearance

import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.example.gliptosapp.R

fun ViewGroup.applyAccessibilityPreferences() {
    applyAccessibilityRecursively()
}

private fun ViewGroup.applyAccessibilityRecursively() {

    val typeface = ResourcesCompat.getFont(
        context,
        when (FontPreferences.getFamily(context)) {
            FontFamily.DEFAULT -> R.font.patrick_hand
            FontFamily.DYSLEXIA -> R.font.opendyslexic
        }
    ) ?: return

    for (i in 0 until childCount) {

        val child = getChildAt(i)

        when (child) {

            is TextView -> {

                child.typeface = typeface
                val originalSize =
                    child.textSize /
                            child.resources.displayMetrics.scaledDensity
                Log.d("FONT", "originalSize=$originalSize")
                TypographyManager.apply(
                    child,
                    originalSize
                )
            }
            is ViewGroup -> child.applyAccessibilityRecursively()
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