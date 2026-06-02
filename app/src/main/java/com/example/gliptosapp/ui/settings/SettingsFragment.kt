package com.example.gliptosapp.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.gliptosapp.databinding.FragmentSettingsBinding
import com.example.gliptosapp.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import com.example.gliptosapp.R
@AndroidEntryPoint
class SettingsFragment : BaseFragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSettingsBinding.inflate(
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
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        changeFontsSize()

        updateFontSelection(
            FontPreferences.get(requireContext())
        )
    }

    private fun changeFontsSize() {

        binding.cardFontSmall.setOnClickListener {
            selectFont(FontScale.SMALL)
        }

        binding.cardFontMedium.setOnClickListener {
            selectFont(FontScale.MEDIUM)
        }

        binding.cardFontLarge.setOnClickListener {
            selectFont(FontScale.LARGE)
        }
    }
    private fun selectFont(scale: FontScale) {

        FontPreferences.save(requireContext(), scale)

        updateFontSelection(
            FontPreferences.get(requireContext())
        )
        requireActivity().recreate()
    }

    private fun updateFontSelection(scale: FontScale) {

        val selectedBackground =
            ContextCompat.getColor(requireContext(), R.color.fondo1)

        val normalBackground =
            ContextCompat.getColor(requireContext(), android.R.color.white)

        val selectedText =
            ContextCompat.getColor(requireContext(), android.R.color.white)

        val normalText =
            ContextCompat.getColor(requireContext(), android.R.color.black)

        binding.cardFontSmall.setCardBackgroundColor(normalBackground)
        binding.cardFontMedium.setCardBackgroundColor(normalBackground)
        binding.cardFontLarge.setCardBackgroundColor(normalBackground)

        binding.tvFontSmall.setTextColor(normalText)

        // Necesitas agregar ids a estos TextView
        binding.tvFontMedium.setTextColor(normalText)
        binding.tvFontLarge.setTextColor(normalText)

        when (scale) {

            FontScale.SMALL -> {
                binding.cardFontSmall.setCardBackgroundColor(selectedBackground)
                binding.tvFontSmall.setTextColor(selectedText)
            }

            FontScale.MEDIUM -> {
                binding.cardFontMedium.setCardBackgroundColor(selectedBackground)
                binding.tvFontMedium.setTextColor(selectedText)
            }

            FontScale.LARGE -> {
                binding.cardFontLarge.setCardBackgroundColor(selectedBackground)
                binding.tvFontLarge.setTextColor(selectedText)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}