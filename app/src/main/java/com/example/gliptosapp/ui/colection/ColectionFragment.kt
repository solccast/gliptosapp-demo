package com.example.gliptosapp.ui.colection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gliptosapp.data.Fosil
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listaMock = listOf(
            Fosil("Gliptodonte", true, null),
            Fosil("Tiranosaurio", false, null),
            Fosil("Trilobite", true, null)
        )

        binding.listaFosiles.layoutManager = LinearLayoutManager(requireContext())
        binding.listaFosiles.adapter = FosilAdapter(listaMock)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}