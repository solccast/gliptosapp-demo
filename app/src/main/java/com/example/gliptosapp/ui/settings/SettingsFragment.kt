package com.example.gliptosapp.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.gliptosapp.databinding.FragmentSettingsBinding
import com.example.gliptosapp.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import com.example.gliptosapp.R
import com.example.gliptosapp.ui.settings.appearance.FontFamily
import com.example.gliptosapp.ui.settings.sound.SoundManager
import com.example.gliptosapp.ui.settings.appearance.FontScale
import com.example.gliptosapp.ui.settings.appearance.getThemeColor
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class SettingsFragment : BaseFragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding =
            FragmentSettingsBinding.inflate(
                inflater,
                container,
                false
            )

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(
            view,
            savedInstanceState
        )

        setupInteractionMode()
        setupListeners()
        observeUiState()
        observeEvents()
    }

    private fun observeUiState() {

        viewLifecycleOwner.lifecycleScope.launch {

            repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {
                viewModel.uiState.collect { state ->
                    binding.switchContrast.isChecked = state.highContrastEnabled
                    binding.switchSounds.isChecked = state.soundsEnabled
                    binding.switchVibration.isChecked = state.vibrationEnabled
                    binding.switchNarration.isChecked = state.narrationEnabled
                    binding.switchDyslexiaFont.isChecked = state.selectedFontFamily == FontFamily.DYSLEXIA
                    updateFontSelection(state.selectedFont)
                    updateInteractionMode(state.interactionMode)
                }
            }
        }
    }
    private fun observeEvents() {

        viewLifecycleOwner.lifecycleScope.launch {

            repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {

                viewModel.events.collect { event ->

                    when (event) {

                        is SettingsEvent.AccessibilityAnnouncement -> {

                            binding.root
                                .announceForAccessibility(
                                    event.message
                                )
                        }

                        SettingsEvent.RecreateActivity -> { requireActivity().recreate() }
                    }
                }
            }
        }
    }
    private fun setupListeners() {

        binding.cardFontSmall.setOnClickListener {
            viewModel.selectFont(FontScale.SMALL)
        }

        binding.cardFontMedium.setOnClickListener {
            viewModel.selectFont(FontScale.MEDIUM)
        }

        binding.cardFontLarge.setOnClickListener {
            viewModel.selectFont(FontScale.LARGE)
        }
        binding.switchNarration.setOnCheckedChangeListener { _, enabled ->
            viewModel.toggleNarration(enabled)
        }
        binding.switchContrast.setOnCheckedChangeListener { _, enabled ->
            viewModel.toggleContrast(enabled)
        }
        binding.switchSounds.setOnCheckedChangeListener { _, enabled ->
                viewModel.toggleSounds(enabled)
            SoundManager.refreshAudioState(requireContext())
        }
        binding.switchVibration.setOnCheckedChangeListener { _, enabled ->
            viewModel.toggleVibration(enabled)
        }
        binding.switchDyslexiaFont.setOnCheckedChangeListener { _, enabled ->
            viewModel.toggleDyslexiaFont(enabled)
        }
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

  private fun updateFontSelection(scale: FontScale) {

        val selectedBackground =
            requireContext().getThemeColor(R.attr.colorAppPrimary)

        val normalBackground =
            ContextCompat.getColor(requireContext(), android.R.color.white)

        val selectedText =
            ContextCompat.getColor(requireContext(), android.R.color.white)

        val normalText =
            ContextCompat.getColor(requireContext(), android.R.color.black)

        val options = listOf(
            FontOption(
                FontScale.SMALL,
                binding.cardFontSmall,
                binding.tvFontSmall,
                "pequeño"
            ),
            FontOption(
                FontScale.MEDIUM,
                binding.cardFontMedium,
                binding.tvFontMedium,
                "mediano"
            ),
            FontOption(
                FontScale.LARGE,
                binding.cardFontLarge,
                binding.tvFontLarge,
                "grande"
            )
        )

        options.forEach { option ->

            val selected = option.scale == scale

            option.card.setCardBackgroundColor(
                if (selected) selectedBackground
                else normalBackground
            )

            option.text.setTextColor(
                if (selected) selectedText
                else normalText
            )

            option.card.contentDescription =
                buildString {
                    append("Soporte visual. Tamaño de texto ")
                    append(option.description)

                    if (selected) {
                        append(". Seleccionado")
                    }
                }
        }
    }


    private fun announceAccessibility(message: String) {

        _binding?.root?.announceForAccessibility(message)
    }

    override fun onResume() {
        super.onResume()

        binding.root.postDelayed({
            announceAccessibility(
                "Pantalla de configuración. Contiene opciones de interacción, soporte visual, apoyo auditivo e inclusión."
            )
        }, 500)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupInteractionMode() {

        binding.cardDragMode.setOnClickListener {

            viewModel.selectInteractionMode(
                InteractionMode.PIECE_FIRST
            )
        }

        binding.cardTapMode.setOnClickListener {

            viewModel.selectInteractionMode(
                InteractionMode.DESTINATION_FIRST
            )
        }
    }
    private fun updateInteractionMode(
        mode: InteractionMode
    ) {

        val dragSelected =
            mode == InteractionMode.PIECE_FIRST

        binding.indicatorDragMode.visibility =
            if (dragSelected) View.VISIBLE
            else View.INVISIBLE

        binding.indicatorTapMode.visibility =
            if (dragSelected) View.INVISIBLE
            else View.VISIBLE

        binding.cardDragMode.strokeWidth =
            if (dragSelected) 6 else 2

        binding.cardTapMode.strokeWidth =
            if (dragSelected) 2 else 6

        binding.cardDragMode.animate()
            .scaleX(if (dragSelected) 1.02f else 1f)
            .scaleY(if (dragSelected) 1.02f else 1f)
            .setDuration(150)
            .start()

        binding.cardTapMode.animate()
            .scaleX(if (dragSelected) 1f else 1.02f)
            .scaleY(if (dragSelected) 1f else 1.02f)
            .setDuration(150)
            .start()

        binding.cardDragMode.contentDescription =
            if (dragSelected)
                "Arrastrar piezas. Seleccionado"
            else
                "Arrastrar piezas"

        binding.cardTapMode.contentDescription =
            if (dragSelected)
                "Tocar pieza y destino"
            else
                "Tocar pieza y destino. Seleccionado"
    }
}

private data class FontOption(
    val scale: FontScale,
    val card: MaterialCardView,
    val text: TextView,
    val description: String
)
