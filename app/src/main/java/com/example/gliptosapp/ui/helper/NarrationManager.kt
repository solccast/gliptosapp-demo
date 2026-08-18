package com.example.gliptosapp.ui.helper

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

object NarrationManager : TextToSpeech.OnInitListener {

    private const val TAG = "NARRATION"

    private var textToSpeech: TextToSpeech? = null
    private var initialized = false

    fun initialize(context: Context) {
        if (textToSpeech != null) return

        textToSpeech = TextToSpeech(
            context.applicationContext,
            this
        )
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = textToSpeech?.setLanguage(Locale("es", "AR"))

            initialized = result != TextToSpeech.LANG_MISSING_DATA &&
                    result != TextToSpeech.LANG_NOT_SUPPORTED

            if (!initialized) {
                Log.e(TAG, "Idioma español no soportado")
            }
        } else {
            Log.e(TAG, "No se pudo inicializar TextToSpeech")
        }
    }

    fun speak(context: Context, text: String) {
        if (text.isBlank()) return

        initialize(context)

        if (!initialized) return

        textToSpeech?.speak(
            text,
            TextToSpeech.QUEUE_FLUSH,
            null,
            "kira_${System.currentTimeMillis()}"
        )
    }

    fun stop() {
        textToSpeech?.stop()
    }

    fun release() {
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        textToSpeech = null
        initialized = false
    }
}
