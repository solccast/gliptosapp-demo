package com.example.gliptosapp.ui.detailFosile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.gliptosapp.R
import com.example.gliptosapp.data.Fosil
import com.example.gliptosapp.databinding.FragmentExtraInfoFosileBinding
import com.example.gliptosapp.ui.ra.RAFosilActivity
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

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        val nombre = args.nombreFosil

        binding.tituloFosil.text = nombre

        binding.descripcionFosil.text = "Este fósil es muy interesante..."
        binding.infoExtra.text = "Época: Pleistoceno\nDieta: Herbívoro"

        val listaMock = listOf(
            Fosil("Gliptodonte", true, R.drawable.gliptodonte, null),
            Fosil("Tiranosaurio", false, null, null),
            Fosil("Trilobite", true, null, null)
        )

        val fosil = listaMock.find { it.nombre == nombre }

        fosil?.img?.let {
            binding.imagenFosil.setImageResource(it)
            binding.imagenFosil.contentDescription = "Imagen del fósil $nombre"
        } ?: run {
            binding.imagenFosil.contentDescription = "Imagen no disponible del fósil $nombre"
        }

        binding.btnJugar.setOnClickListener {
            // TODO navegación o acción
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