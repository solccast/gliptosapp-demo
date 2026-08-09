package com.example.gliptosapp.ui.detailFosile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.gliptosapp.databinding.FragmentExtraInfoFosileBinding
import com.example.gliptosapp.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import androidx.fragment.app.viewModels
import com.example.gliptosapp.R
import com.example.gliptosapp.ui.helper.AvisoDialog
import com.example.gliptosapp.ui.visor3d.Visor3DActivity

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

        binding.btnAyuda.setOnClickListener {
            AvisoDialog.mostrar(context = requireContext(),
                mensaje = getString(R.string.ayuda_extra_info_fosile))
        }

        val fosilId = args.fosilId
        val nombreFosil = args.nombreFosil
        viewModel.cargarFosil(fosilId)

        viewModel.fosil.observe(viewLifecycleOwner) { fosil ->
            if (fosil == null) {
                return@observe
            }

            binding.tituloFosil.text = nombreFosil
            binding.descripcionFosil.text = fosil.descripcion
            binding.textoNota.text = "Época: Pleistoceno\nDieta: Herbívoro"

            val resId = resolverDrawable(requireContext(), fosil.obtenerImagen())
            binding.imagenFosil.setImageResource(resId)
            binding.imagenFosil.contentDescription = "Imagen del fósil ${nombreFosil}"
        }

        binding.btnJugar.setOnClickListener {
            findNavController().navigate(
                ExtraInfoFosileFragmentDirections.actionExtraInfoFosileFragmentToComparativeGameInfoFragment(nombreFosil, fosilId)
            )
        }

        viewModel.game.observe(viewLifecycleOwner) { game ->
            val desbloqueada = game?.realizada == true
            binding.overlayBloqueo.isVisible = !desbloqueada
            binding.btnJugar.isVisible = !desbloqueada
            binding.textoNota.text = if (desbloqueada) {
                game?.infoExtra
            } else {
                getString(R.string.texto_ilegible_placeholder)
            }
        }

        binding.btnVer.setOnClickListener {
            startActivity(
                Visor3DActivity.newIntent(
                    context = requireContext(),
                    modelPath = "models/duck.glb",
                    titulo = args.nombreFosil
                )
            )
        }
        binding.btnVer.contentDescription =
            "Ver el fósil ${args.nombreFosil} en 3D"
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