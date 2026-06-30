package com.example.gliptosapp.ui.comparativeGameInfo

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.gliptosapp.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.example.gliptosapp.R
import com.example.gliptosapp.databinding.FragmentComparativeGameInfoBinding
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.launch
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.delay

@AndroidEntryPoint
class ComparativeGameInfoFragment : BaseFragment() {

    private var _binding: FragmentComparativeGameInfoBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ComparativeGameInfoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentComparativeGameInfoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnAjustes.setOnClickListener {
            findNavController().navigate(R.id.action_comparativeGameInfoFragment_to_settingsFragment)
        }

        binding.cardOpcionA.setOnClickListener { viewModel.seleccionarOpcion(0) }
        binding.cardOpcionB.setOnClickListener { viewModel.seleccionarOpcion(1) }

        binding.btnVerInformacion.setOnClickListener {
            findNavController().popBackStack()
        }

        observarEstado()
    }

    private fun observarEstado() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { estado -> renderizarEstado(estado) }
            }
        }
    }

    private fun renderizarEstado(estado: ComparativeGameUiState) {
        val juego = estado.juego ?: return
        val (opcionA, opcionB) = juego.opciones

        binding.tvPregunta.text = juego.textoPregunta
        binding.ivOpcionA.setImageResource(opcionA.imgOption)
        binding.tvOpcionA.text = opcionA.texto
        binding.ivOpcionB.setImageResource(opcionB.imgOption)
        binding.tvOpcionB.text = opcionB.texto

        binding.cardOpcionA.isClickable = !estado.completado
        binding.cardOpcionB.isClickable = !estado.completado
        binding.btnVerInformacion.visibility = if (estado.completado) View.VISIBLE else View.GONE

        if (estado.indiceSeleccionado == null) {
            resetearEstilo(binding.cardOpcionA)
            resetearEstilo(binding.cardOpcionB)
            binding.tvDialogo.setText(R.string.como_jugar_minijuego)
            return
        }

        val cards = listOf(binding.cardOpcionA, binding.cardOpcionB)
        cards.forEachIndexed { index, card ->
            if (index == estado.indiceSeleccionado) {
                pintarResultado(card, estado.esCorrecta == true)
            } else {
                resetearEstilo(card)
            }
        }

        binding.tvDialogo.setText(
            if (estado.esCorrecta == true) R.string.feedback_correcto
            else R.string.feedback_incorrecto
        )
    }

    private fun pintarResultado(card: MaterialCardView, correcta: Boolean) {
        val colorFondoRes = if (correcta) R.color.fondo_correcto else R.color.fondo_incorrecto
        val colorBordeRes = if (correcta) R.color.borde_correcto else R.color.borde_incorrecto
        card.setCardBackgroundColor(
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), colorFondoRes))
        )
        card.strokeColor = ContextCompat.getColor(requireContext(), colorBordeRes)
    }

    private fun resetearEstilo(card: MaterialCardView) {
        card.setCardBackgroundColor(
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.fondo_polaroid))
        )
        card.strokeColor = ContextCompat.getColor(requireContext(), R.color.fondo2)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}