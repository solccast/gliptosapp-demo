package com.example.gliptosapp.ui.colection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gliptosapp.databinding.FragmentColectionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ColectionFragment : Fragment() {
    private var _binding: FragmentColectionBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: FosilAdapter
    private val colectionViewModel by viewModels<ColectionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentColectionBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.listaFosiles.layoutManager = LinearLayoutManager(requireContext())

        adapter = FosilAdapter(emptyList()) { fosil ->
            val action = ColectionFragmentDirections
                .actionColectionFragmentToExtraInfoFosileFragment(fosil.nombre)

            findNavController().navigate(action)
        }

        binding.listaFosiles.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.listaFosiles.adapter = adapter

        colectionViewModel.fosiles.observe(viewLifecycleOwner) { lista ->
            adapter.updateList(lista)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}