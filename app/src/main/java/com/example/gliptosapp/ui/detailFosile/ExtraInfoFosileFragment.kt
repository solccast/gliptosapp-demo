package com.example.gliptosapp.ui.detailFosile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            binding.tituloFosil.text = nombreFosil
            binding.descripcionFosil.text = state.fosil.descripcion

            val resId = resolverDrawable(requireContext(), state.fosil.obtenerImagen())
            binding.imagenFosil.setImageResource(resId)
            binding.imagenFosil.contentDescription = "Imagen del fósil ${nombreFosil}"

            binding.overlayBloqueo.isVisible = !state.desbloqueada
            binding.btnJugar.isVisible = !state.desbloqueada
            binding.contenedorDatosFosil.isVisible = state.desbloqueada

            binding.textoNota.text = if (state.desbloqueada) {
                state.infoExtra
            } else {
                getString(R.string.texto_ilegible_placeholder)
            }

            if (state.desbloqueada) {
                with(state.fosil) {
                    bindearDato(binding.cardEpoca.root, R.string.dato_epoca, epoca)
                    bindearDato(binding.cardHabitat.root, R.string.dato_habitat, habitat)
                    bindearDato(binding.cardTamano.root, R.string.dato_tamano, tamano)
                    bindearDato(binding.cardPeso.root, R.string.dato_peso, peso)
                    bindearDato(binding.cardDieta.root, R.string.dato_dieta, dieta)
                }
            }
        }

        binding.btnJugar.setOnClickListener {
            findNavController().navigate(
                ExtraInfoFosileFragmentDirections.actionExtraInfoFosileFragmentToComparativeGameInfoFragment(nombreFosil, fosilId)
            )
        }

        binding.btnVer.setOnClickListener {
            startActivity(
                Visor3DActivity.newIntent(
                    context = requireContext(),
                    modelPath = "models/rex.glb",
                    titulo = args.nombreFosil
                )
            )
        }
        binding.btnVer.contentDescription = "Ver el fósil ${args.nombreFosil} en 3D"
    }

    private fun bindearDato(card: View, labelResId: Int, valor: String) {
        card.findViewById<TextView>(R.id.etiquetaDato).text = getString(labelResId)
        card.findViewById<TextView>(R.id.valorDato).text = valor
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