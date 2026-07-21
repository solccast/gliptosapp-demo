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

        viewModel.fosil.observe(viewLifecycleOwner) { fosil ->
            if (fosil == null) {
                // El fósil no se encontró en la base (nombre con typo, o aún no sembrado).
                // Ajustá esto según cómo quieras comunicar el error en tu UI.
                binding.descripcionFosil.text = "No encontrado..."
                return@observe
            }

            binding.tituloFosil.text = nombre
            binding.descripcionFosil.text = fosil.descripcion
            binding.infoExtra.text = "Época: Pleistoceno\nDieta: Herbívoro"

            val resId = resolverDrawable(requireContext(), fosil.obtenerImagen())
            binding.imagenFosil.setImageResource(resId)
            binding.imagenFosil.contentDescription = "Imagen del fósil $nombre"
        }

        binding.btnJugar.setOnClickListener {
            findNavController().navigate(
                ExtraInfoFosileFragmentDirections.actionExtraInfoFosileFragmentToComparativeGameInfoFragment(args.nombreFosil)
            )
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

    /**
     * Convierte el nombre del drawable (guardado como String en la entidad Fosil)
     * al resource id Int que espera setImageResource.
     */
    private fun resolverDrawable(context: android.content.Context, nombreRecurso: String): Int {
        val resId = context.resources.getIdentifier(nombreRecurso, "drawable", context.packageName)
        return if (resId != 0) resId else R.drawable.gliptodonte
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}