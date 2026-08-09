package com.example.gliptosapp.ui.home

import android.content.Context
import android.util.TypedValue
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.example.gliptosapp.R
import com.example.gliptosapp.databinding.FragmentInitBinding
import com.example.gliptosapp.ui.BaseFragment
import com.example.gliptosapp.ui.helper.AvisoDialog
import com.example.gliptosapp.ui.settings.appearance.applyAccessibilityPreferences
class InitFragment : BaseFragment() {
    private var _binding: FragmentInitBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInitBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()

        binding.btnExcavacion.setOnClickListener {
            navController.navigate(R.id.action_initFragment_to_excavacionActivity)
        }

        binding.btnExcavacion.setOnClickListener {
            // Llama a la acción que abre la ExcavacionActivity declarada en el grafo
            findNavController().navigate(R.id.action_initFragment_to_excavacionActivity)
        }

        binding.btnColeccion.setOnClickListener {
            navController.navigate(R.id.colectionFragment)
        }

        binding.btnAjustes.setOnClickListener {
            navController.navigate(R.id.settingsFragment)
        }

        binding.btnRecursos.setOnClickListener {
            navController.navigate(R.id.action_initFragment_to_recursosFragment)
        }

        (binding.root as ViewGroup).applyAccessibilityPreferences()

        binding.btnInfo.setOnClickListener {
            AvisoDialog.mostrar(requireContext(),getString(R.string.ayuda_inicio))
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(
            "FONT",
            binding.btnExcavacion.textSize.toString()
        )
        aplicarConfiguracionDeAccesibilidad()
        Log.d(
            "FONT",
            binding.btnExcavacion.textSize.toString()
        )
    }

    private fun aplicarConfiguracionDeAccesibilidad() {
        // Validación de seguridad para asegurar que el fragmento está adjunto a la actividad
        val contexto = context ?: return

        val sharedPreferences = contexto.getSharedPreferences("AjustesApp", Context.MODE_PRIVATE)
        val modoTamaño = sharedPreferences.getString("tamano_interfaz", "medium")

        // CORRECCIÓN: Sintaxis correcta con puntos (R.dimen.id)
        val (altoDimen, textoSizeDimen) = if (modoTamaño == "large") {
            Pair(R.dimen.boton_alto_large, R.dimen.texto_boton_large)
        } else {
            Pair(R.dimen.boton_alto_medium, R.dimen.texto_boton_medium)
        }

        // Obtener los valores reales desde recursos
        val altoEnPixeles = resources.getDimensionPixelSize(altoDimen)

        // Convertimos los píxeles a escala SP de forma precisa para el método .setTextSize()
        val tamanoTextoSp = resources.getDimension(textoSizeDimen) / resources.displayMetrics.scaledDensity

        // Lista de botones a los que se les aplicará el cambio de accesibilidad
        val listaBotones = listOf(binding.btnExcavacion, binding.btnColeccion, binding.btnAjustes, binding.btnRecursos)

        listaBotones.forEach { boton ->
            // 1. Modificar dinámicamente el alto manteniendo los LayoutParams del LinearLayout interno
            val params = boton.layoutParams as? LinearLayout.LayoutParams
            params?.let {
                it.height = altoEnPixeles
                boton.layoutParams = it
            }

            // 2. Modificar el tamaño del texto usando unidades SP de manera segura
            boton.setTextSize(TypedValue.COMPLEX_UNIT_SP, tamanoTextoSp)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Evita memory leaks (Fuga de memoria), una excelente práctica de desarrollo
    }
}