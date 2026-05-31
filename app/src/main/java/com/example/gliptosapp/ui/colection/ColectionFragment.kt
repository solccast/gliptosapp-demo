package com.example.gliptosapp.ui.colection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gliptosapp.R
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
            Fosil("Gliptodonte", true, R.drawable.gliptodonte, null),
            Fosil("Tiranosaurio", false, null,null),
            Fosil("Trilobite", true, null,null)
        )

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.listaFosiles.layoutManager = LinearLayoutManager(requireContext())
        binding.listaFosiles.adapter = FosilAdapter(listaMock) { fosil ->

            val action = ColectionFragmentDirections
                .actionColectionFragmentToExtraInfoFosileFragment(fosil.nombre)

            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}