package com.example.gliptosapp.ui.settings

sealed class SettingsEvent {

    data class AccessibilityAnnouncement(
        val message: String
    ) : SettingsEvent()

    object RecreateActivity : SettingsEvent()
}