package com.example.gliptosapp.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.gliptosapp.databinding.FragmentSettingsBinding
import com.example.gliptosapp.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import com.example.gliptosapp.R
import com.google.android.material.card.MaterialCardView

@AndroidEntryPoint
class SettingsFragment : BaseFragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private var fontChangeInProgress = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        setupFontButtons()
        setupAccessibilitySwitches()
        setupInteractionModeAccessibility()

        updateFontSelection(
            FontPreferences.get(requireContext())
        )
    }

    private fun setupFontButtons() {

        mapOf(
            binding.cardFontSmall to FontScale.SMALL,
            binding.cardFontMedium to FontScale.MEDIUM,
            binding.cardFontLarge to FontScale.LARGE
        ).forEach { (view, scale) ->
            view.setOnClickListener { selectFont(scale) }
        }
    }

    private fun selectFont(scale: FontScale) {

        if (fontChangeInProgress) return

        val currentScale =
            FontPreferences.get(requireContext())

        if (currentScale == scale) return

        fontChangeInProgress = true

        FontPreferences.save(
            requireContext(),
            scale
        )

        updateFontSelection(scale)

        announceAccessibility(
            "Tamaño de texto cambiado a ${scale.displayName}"
        )

        recreateAfterFontChange()
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

    private fun setupAccessibilitySwitches() {

        binding.switchContrast.isChecked =
            ContrastPreferences.isEnabled(requireContext())

        binding.switchContrast.setOnCheckedChangeListener { _, enabled ->

            ContrastPreferences.save(
                requireContext(),
                enabled
            )

            announceAccessibility(
                if (enabled)
                    "Alto contraste activado"
                else
                    "Alto contraste desactivado"
            )

            recreateActivity()
        }

        setupSwitch(
            binding.switchNarration,
            "Narración"
        )

        setupSwitch(
            binding.switchSounds,
            "Sonidos"
        )

        setupSwitch(
            binding.switchVibration,
            "Vibración"
        )

        setupSwitch(
            binding.switchLsa,
            "Lengua de Señas Argentina"
        )
    }

    private fun setupSwitch(
        switch: CompoundButton,
        label: String
    ) {
        switch.setOnCheckedChangeListener { _, enabled ->
            announceAccessibility(
                "$label ${if (enabled) "activada" else "desactivada"}"
            )
        }
    }

    private fun setupInteractionModeAccessibility() {

        binding.rgInteractionMode.setOnCheckedChangeListener { _, checkedId ->

            val message = when (checkedId) {

                binding.rbDragToClean.id ->
                    "Modo de interacción cambiado a arrastrar y mover"

                binding.rbTapToClean.id ->
                    "Modo de interacción cambiado a tocar y limpiar"

                else -> return@setOnCheckedChangeListener
            }

            announceAccessibility(message)
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

    private fun recreateActivity() {

        binding.root.postDelayed({

            if (!isAdded) return@postDelayed

            requireActivity().recreate()

        }, 500)
    }

    private fun recreateAfterFontChange() {

        binding.cardFontSmall.isEnabled = false
        binding.cardFontMedium.isEnabled = false
        binding.cardFontLarge.isEnabled = false

        recreateActivity()
    }
}

private data class FontOption(
    val scale: FontScale,
    val card: MaterialCardView,
    val text: TextView,
    val description: String
)

private val FontScale.label: String
    get() = when (this) {
        FontScale.SMALL -> "pequeño"
        FontScale.MEDIUM -> "mediano"
        FontScale.LARGE -> "grande"
    }