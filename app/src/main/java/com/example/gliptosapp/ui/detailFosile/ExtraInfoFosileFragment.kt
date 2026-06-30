package com.example.gliptosapp.ui.detailFosile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.gliptosapp.databinding.FragmentExtraInfoFosileBinding
import com.example.gliptosapp.ui.BaseFragment
import com.example.gliptosapp.ui.ra.RAFosilActivity
import dagger.hilt.android.AndroidEntryPoint
import androidx.fragment.app.viewModels
import com.example.gliptosapp.R

@AndroidEntryPoint
class ExtraInfoFosileFragment : BaseFragment() {

    private var _binding: FragmentExtraInfoFosileBinding? = null
    private val binding get() = _binding!!
    private val args: ExtraInfoFosileFragmentArgs by navArgs()
    private val viewModel by viewModels<ExtraInfoFosileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExtraInfoFosileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            binding.btnBack.setOnClickListener {
                findNavController().popBackStack()
            }

            binding.btnAjustes.setOnClickListener {
                findNavController().navigate(R.id.action_extraInfoFosileFragment_to_settingsFragment)
            }

            val nombre = args.nombreFosil
            viewModel.cargarFosil(nombre)

            viewModel.fosil.observe(viewLifecycleOwner){fosil ->
            binding.tituloFosil.text = nombre

            binding.descripcionFosil.text = fosil.descripcion // TODO: reemplazar por el dato real
            binding.infoExtra.text = "Época: Pleistoceno\nDieta: Herbívoro"
            fosil.obtenerImagen().let {
                binding.imagenFosil.setImageResource(it)
                binding.imagenFosil.contentDescription = "Imagen del fósil $nombre"
            }
        }

        binding.btnJugar.setOnClickListener {
            findNavController().navigate(ExtraInfoFosileFragmentDirections.actionExtraInfoFosileFragmentToComparativeGameInfoFragment("Glipto?"))
        }

        binding.btnVer.setOnClickListener {
            // TODO realidad aumentada
            val intent = Intent(requireContext(), RAFosilActivity::class.java)
            intent.putExtra("nombreFosil", args.nombreFosil)
            startActivity(intent)
        }
        binding.btnVer.contentDescription =
            "Ver el fósil ${args.nombreFosil} en realidad aumentada"
    }
}