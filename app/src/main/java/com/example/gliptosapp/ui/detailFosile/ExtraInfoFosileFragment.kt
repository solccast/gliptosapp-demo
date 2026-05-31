package com.example.gliptosapp.ui.detailFosile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.gliptosapp.databinding.FragmentExtraInfoFosileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExtraInfoFosileFragment : Fragment() {

    private var _binding: FragmentExtraInfoFosileBinding? = null
    private val binding get() = _binding!!
    private val args: ExtraInfoFosileFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExtraInfoFosileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val nombre = args.nombreFosil

        binding.tituloFosil.text = nombre

        binding.descripcionFosil.text = "Este fósil es muy interesante..."
        binding.infoExtra.text = "Época: Pleistoceno\nDieta: Herbívoro"

        // accesibilidad dinámica
        binding.imagenFosil.contentDescription =
            "Imagen del fósil $nombre"

        binding.btnJugar.setOnClickListener {
            // TODO navegación o acción
        }

        binding.btnVer.setOnClickListener {
            // TODO realidad aumentada
        }
    }
}