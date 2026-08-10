package com.example.gliptosapp.ui.helper

import android.content.Context
import com.example.gliptosapp.repository.SettingsRepository
import javax.inject.Inject

class KiraNarration @Inject constructor(
    private val settingsRepository: SettingsRepository
) {

    fun speak(
        context: Context,
        message: String
    ) {
        if (message.isBlank()) return

        if (!settingsRepository.isNarrationEnabled()) return

        NarrationManager.speak(
            context,
            message
        )
    }
}
