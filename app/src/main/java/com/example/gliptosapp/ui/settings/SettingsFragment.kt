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

                    updateFontSelection(
                        state.selectedFont
                    )

                    binding.switchContrast.isChecked =
                        state.highContrastEnabled
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

                        SettingsEvent.RecreateActivity -> {

                            requireActivity().recreate()
                        }
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

        binding.switchContrast.setOnCheckedChangeListener { _, enabled ->
            viewModel.toggleContrast(enabled)
        }

        binding.backButton.btnBack.setOnClickListener {
            findNavController().navigateUp()
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

    private fun recreateActivity() {

        binding.root.postDelayed({

            if (!isAdded) return@postDelayed

            requireActivity().recreate()

        }, 500)
    }
}

private data class FontOption(
    val scale: FontScale,
    val card: MaterialCardView,
    val text: TextView,
    val description: String
)
