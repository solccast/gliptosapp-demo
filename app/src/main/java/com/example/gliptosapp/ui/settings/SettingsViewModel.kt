package com.example.gliptosapp.ui.settings

import androidx.lifecycle.ViewModel
import com.example.gliptosapp.repository.SettingsRepository
import com.example.gliptosapp.ui.settings.appearance.FontFamily
import com.example.gliptosapp.ui.settings.appearance.FontScale
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: SettingsRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(SettingsUiState())

    val uiState: StateFlow<SettingsUiState> =
        _uiState

    private val _events =
        MutableSharedFlow<SettingsEvent>(extraBufferCapacity = 1)

    val events =
        _events.asSharedFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {

        _uiState.value =
            _uiState.value.copy(
                selectedFont = repository.getFontScale(),
                selectedFontFamily = repository.getFontFamily(),
                highContrastEnabled = repository.isHighContrastEnabled(),
                interactionMode = repository.getInteractionMode(),
                narrationEnabled = repository.isNarrationEnabled(),
                soundsEnabled = repository.isSoundEnabled(),
                vibrationEnabled = repository.isVibrationEnabled()
            )
    }

    fun selectFont(scale: FontScale) {

        if (_uiState.value.selectedFont == scale)
            return

        repository.saveFontScale(scale)

        _uiState.value =
            _uiState.value.copy(
                selectedFont = scale
            )

        emitAnnouncement(
            "Tamaño de texto cambiado a ${scale.displayName}"
        )

        emitRecreate()
    }

    fun toggleNarration(enabled: Boolean) {
        _uiState.update { it.copy(narrationEnabled = enabled) }
        repository.saveNarrationEnabled(enabled)
    }

    fun toggleContrast(enabled: Boolean) {

        repository.saveHighContrast(enabled)

        _uiState.value =
            _uiState.value.copy(
                highContrastEnabled = enabled
            )

        emitAnnouncement(
            if (enabled)
                "Alto contraste activado"
            else
                "Alto contraste desactivado"
        )

        emitRecreate()
    }

    fun toggleVibration(enabled: Boolean) {
        repository.saveVibrationEnabled(enabled)
        _uiState.value = _uiState.value.copy(vibrationEnabled = enabled)

        emitAnnouncement(
            if (enabled)
                "Vibración activada"
            else
                "Vibración desactivada"
        )
    }

    fun selectInteractionMode(
        mode: InteractionMode
    ) {

        if (_uiState.value.interactionMode == mode)
            return

        repository.saveInteractionMode(mode)

        _uiState.value =
            _uiState.value.copy(
                interactionMode = mode
            )

        emitAnnouncement(
            when (mode) {

                InteractionMode.PIECE_FIRST ->
                    "Modo tocar pieza y destino seleccionado"

                InteractionMode.DESTINATION_FIRST ->
                    "Modo tocar destino y pieza seleccionado"
            }
        )
    }

    fun selectFontFamily(
        family: FontFamily
    ) {

        if (_uiState.value.selectedFontFamily == family)
            return

        repository.saveFontFamily(family)

        _uiState.update {
            it.copy(selectedFontFamily = family)
        }

        emitAnnouncement(
            if (family == FontFamily.DYSLEXIA)
                "Fuente para dislexia activada"
            else
                "Fuente predeterminada activada"
        )

        emitRecreate()
    }


    fun toggleDyslexiaFont(enabled: Boolean) =
        selectFontFamily(
            if (enabled) FontFamily.DYSLEXIA else FontFamily.DEFAULT
        )

    fun toggleSounds(
        enabled: Boolean
    ) {

        repository.saveSoundEnabled(
            enabled
        )

        _uiState.value =
            _uiState.value.copy(
                soundsEnabled = enabled
            )

        emitAnnouncement(
            if (enabled)
                "Sonidos activados"
            else
                "Sonidos desactivados"
        )
    }

    private fun emitAnnouncement(message: String){
        _events.tryEmit(SettingsEvent.AccessibilityAnnouncement(message))
    }

    private fun emitRecreate() {
        _events.tryEmit(SettingsEvent.RecreateActivity)
    }
}