package com.example.gliptosapp.ui.ra

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gliptosapp.databinding.FragmentRAumentadaBinding
import com.example.gliptosapp.ui.BaseFragment

class RAumentadaFragment : BaseFragment() {
    private var _binding: FragmentRAumentadaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRAumentadaBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

}