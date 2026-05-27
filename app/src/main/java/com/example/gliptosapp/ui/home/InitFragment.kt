package com.example.gliptosapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.gliptosapp.R
import com.example.gliptosapp.databinding.FragmentInitBinding

class InitFragment : Fragment() {
    private var _binding: FragmentInitBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInitBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()

        binding.btnColeccion.setOnClickListener {
            navController.navigate(R.id.colectionFragment)
        }

        binding.btnAjustes.setOnClickListener {
            navController.navigate(R.id.settingsFragment)
        }
    }

}