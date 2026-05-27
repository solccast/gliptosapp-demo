package com.example.gliptosapp.ui.colection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.gliptosapp.databinding.FragmentColectionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ColectionFragment : Fragment() {
    private var _binding: FragmentColectionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentColectionBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

}