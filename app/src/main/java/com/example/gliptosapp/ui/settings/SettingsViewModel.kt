package com.example.gliptosapp.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gliptosapp.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: SettingsRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(SettingsUiState())

    val uiState: StateFlow<SettingsUiState> =
        _uiState

    private val _events =
        MutableSharedFlow<SettingsEvent>()

    val events =
        _events.asSharedFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {

        _uiState.value =
            _uiState.value.copy(
                selectedFont = repository.getFontScale(),
                highContrastEnabled =
                    repository.isHighContrastEnabled(),
                interactionMode =
                    repository.getInteractionMode()
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

    private fun emitAnnouncement(
        message: String
    ) {
        viewModelScope.launch {
            _events.emit(
                SettingsEvent.AccessibilityAnnouncement(
                    message
                )
            )
        }
    }

    private fun emitRecreate() {

        viewModelScope.launch {
            _events.emit(
                SettingsEvent.RecreateActivity
            )
        }
    }
}