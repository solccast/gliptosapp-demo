package com.example.gliptosapp.ui.settings.vibration

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log

object VibrationManager {

    fun vibrate(
        context: Context,
        duration: Long = 300
    ) {

        // Respetar la configuración del usuario
        if (!VibrationPreferences.isEnabled(context)) {
            Log.d("VIBRATION", "Vibración deshabilitada por el usuario")
            return
        }

        val vibrator = context.getSystemService(
            Context.VIBRATOR_SERVICE
        ) as Vibrator

        if (!vibrator.hasVibrator()) { return }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    duration,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )

        } else {

            @Suppress("DEPRECATION")
            vibrator.vibrate(duration)
        }
    }
}