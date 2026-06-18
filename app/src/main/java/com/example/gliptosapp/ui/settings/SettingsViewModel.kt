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
                    repository.isHighContrastEnabled()
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

    fun interactionModeChanged(message: String) {

        emitAnnouncement(message)
    }

    fun switchChanged(
        label: String,
        enabled: Boolean
    ) {

        emitAnnouncement(
            "$label ${if (enabled) "activada" else "desactivada"}"
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