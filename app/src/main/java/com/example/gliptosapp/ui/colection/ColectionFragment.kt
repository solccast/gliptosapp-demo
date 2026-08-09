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
import com.example.gliptosapp.ui.helper.AvisoDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ColectionFragment : BaseFragment() {
    private var _binding: FragmentColectionBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: FosilAdapter
    private val colectionViewModel by viewModels<ColectionViewModel>()

    private var avisoInicialMostrado = false

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

        binding.btnAyuda.setOnClickListener {
        }

        adapter = FosilAdapter(
            lista = emptyList(),
            onDetalleClick = { fosil ->
                val action = ColectionFragmentDirections
                    .actionColectionFragmentToExtraInfoFosileFragment(fosil.nombre, fosil.fosil.id)
                findNavController().navigate(action)
            },
            onNoDescubiertoClick = { mostrarAvisoDebeExcavar(getString(R.string.aviso_excavar_fosil_individual)) }
        )

        binding.listaFosiles.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.listaFosiles.adapter = adapter

        colectionViewModel.fosiles.observe(viewLifecycleOwner) { lista ->
            adapter.updateList(lista)

            val todosSinDescubrir = lista.isNotEmpty() && lista.all { !it.descubierto }
            if (todosSinDescubrir && !avisoInicialMostrado) {
                avisoInicialMostrado = true
                mostrarAvisoDebeExcavar(getString(R.string.aviso_excavar_todos_bloqueados))
            }
        }

        binding.btnMapa.setOnClickListener {
            findNavController().navigate(R.id.action_colectionFragment_to_mapaExcavacionActivity)
        }
    }

    private fun mostrarAvisoDebeExcavar(mensaje: String) {
        AvisoDialog.mostrar(
            context = requireContext(),
            mensaje = mensaje
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}