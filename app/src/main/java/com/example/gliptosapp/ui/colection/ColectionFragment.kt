package com.example.gliptosapp.ui.colection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gliptosapp.R
import com.example.gliptosapp.databinding.FragmentColectionBinding
import com.example.gliptosapp.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ColectionFragment : BaseFragment() {
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

        binding.btnAjustes.setOnClickListener {
            findNavController().navigate(R.id.action_colectionFragment_to_settingsFragment)
        }

        binding.listaFosiles.layoutManager = LinearLayoutManager(requireContext())

        adapter = FosilAdapter(emptyList()) { fosil ->
            val action = ColectionFragmentDirections
                .actionColectionFragmentToExtraInfoFosileFragment(fosil.nombre, fosil.fosil.id)

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