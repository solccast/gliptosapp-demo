package com.example.gliptosapp.ui.settings

data class SettingsUiState(
    val selectedFont: FontScale = FontScale.MEDIUM,
    val highContrastEnabled: Boolean = false,
    val narrationEnabled: Boolean = false,
    val soundsEnabled: Boolean = false,
    val vibrationEnabled: Boolean = false,
    val interactionMode: InteractionMode = InteractionMode.PIECE_FIRST
)
enum class InteractionMode {
    PIECE_FIRST,
    DESTINATION_FIRST
}