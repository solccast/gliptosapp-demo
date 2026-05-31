package com.example.gliptosapp.ui.detailFosile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.gliptosapp.databinding.FragmentExtraInfoFosileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExtraInfoFosileFragment : Fragment() {

    private var _binding: FragmentExtraInfoFosileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExtraInfoFosileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}