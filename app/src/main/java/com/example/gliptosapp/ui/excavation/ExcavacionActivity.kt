package com.example.gliptosapp.ui.excavation

import android.os.Build
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.activity.SystemBarStyle
import android.graphics.Color
import androidx.activity.enableEdgeToEdge
import com.example.gliptosapp.R
import com.example.gliptosapp.databinding.ActivityExcavacionBinding

class ExcavacionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExcavacionBinding
    private var estadoActual = 1
    private var herramientaSeleccionada = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExcavacionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.parseColor("#CD4A2C1D")),
            navigationBarStyle = SystemBarStyle.dark(Color.parseColor("#CD4A2C1D"))
        )

        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = false
        windowInsetsController.isAppearanceLightNavigationBars = false

        ViewCompat.setOnApplyWindowInsetsListener(binding.layoutPrincipal) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val margen8dp = (8 * resources.displayMetrics.density).toInt()

            view.setPadding(0, 0, 0, 0)

            val paramsBotones = binding.contenedorBotonesSuperiores.layoutParams as ConstraintLayout.LayoutParams
            paramsBotones.topMargin = bars.top + margen8dp
            binding.contenedorBotonesSuperiores.layoutParams = paramsBotones

            val paramsHerramientas = binding.contenedorHerramientas.layoutParams as ConstraintLayout.LayoutParams
            paramsHerramientas.bottomMargin = bars.bottom + margen8dp
            binding.contenedorHerramientas.layoutParams = paramsHerramientas

            insets
        }

        // Configuración del juego
        configurarHerramientas()
        configurarBotonesSuperiores()
        actualizarDescripcionesAccesibles()
    }

    private fun configurarHerramientas() {
        val listaBotones = listOf(binding.btnPico, binding.btnPala, binding.btnPincel)

        binding.btnPico.setOnClickListener { activarHerramienta("PICO", binding.btnPico, listaBotones) }
        binding.btnPala.setOnClickListener { activarHerramienta("PALA", binding.btnPala, listaBotones) }
        binding.btnPincel.setOnClickListener { activarHerramienta("PINCEL", binding.btnPincel, listaBotones) }
    }

    private fun activarHerramienta(herramienta: String, botonActivo: ImageButton, botones: List<ImageButton>) {
        herramientaSeleccionada = herramienta

        // Marcar visualmente el botón seleccionado
        botones.forEach { it.isSelected = false }
        botonActivo.isSelected = true

        actualizarDescripcionesAccesibles()
        botonActivo.announceForAccessibility("Herramienta $herramienta seleccionada.")

        // Intentar avanzar directamente al seleccionar
        val avanzo = intentarAvanzar()

        // Si avanzó, deseleccionar todos los botones para indicar que hay que elegir de nuevo
        if (avanzo) {
            botones.forEach { it.isSelected = false }
        }
    }

    private fun configurarBotonesSuperiores() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnInfo.setOnClickListener {
            // Por ahora un placeholder; reemplazá con tu diálogo o activity de info
            mostrarDialogo("Información", "Usá las herramientas para desenterrar el fósil paso a paso.")
        }

        binding.btnConfig.setOnClickListener {
            // Placeholder; reemplazá con tu activity de configuración
            mostrarDialogo("Configuración", "Accedelo desde el menú principal....")
        }
    }

    private fun mostrarDialogo(titulo: String, mensaje: String) {
        android.app.AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(mensaje)
            .setPositiveButton("Entendido", null)
            .show()
    }

    // Devuelve true si la herramienta era correcta y se avanzó
    private fun intentarAvanzar(): Boolean {
        return when (estadoActual) {
            1 -> if (herramientaSeleccionada == "PICO") {
                actualizarEstado(R.drawable.gliptodonte_2, "¡Crack! Rompiste la capa superior. ¡Usá el Pico de nuevo!")
                true
            } else { errorFeedback(); false }

            2 -> if (herramientaSeleccionada == "PICO") {
                actualizarEstado(R.drawable.gliptodonte_3, "Piedras removidas. Ahora usá la Pala para limpiar los escombros.")
                true
            } else { errorFeedback(); false }

            3 -> if (herramientaSeleccionada == "PALA") {
                actualizarEstado(R.drawable.gliptodonte_4, "¡Ya se ve la silueta! Usá el Pincel para limpiar el polvo.")
                true
            } else { errorFeedback(); false }

            4 -> if (herramientaSeleccionada == "PINCEL") {
                actualizarEstado(R.drawable.gliptodonte_5, "¡Increíble! Desenterraste un Gliptodonte completo. ¡Sos un gran paleontólogo!")
                finalizarMecanica()
                true
            } else { errorFeedback(); false }

            else -> false
        }
    }


    private fun actualizarEstado(resourceImg: Int, mensajeKira: String) {
        estadoActual++
        binding.imgFosilFondo.setImageResource(resourceImg)
        binding.txtIndicacionKira.text = mensajeKira

        binding.areaExcavacionClick.contentDescription = "Fósil en etapa $estadoActual de 5. $mensajeKira"
        binding.areaExcavacionClick.announceForAccessibility(mensajeKira)
    }

    private fun errorFeedback() {
        val avisoError = "¡Huy! Esa herramienta no sirve acá. Escuchá la indicación de Kira arriba."
        binding.areaExcavacionClick.announceForAccessibility(avisoError)
    }

    private fun actualizarDescripcionesAccesibles() {
        binding.btnPico.contentDescription = if (binding.btnPico.isSelected)
            "Herramienta Pico seleccionada para romper roca" else "Seleccionar herramienta Pico"

        binding.btnPala.contentDescription = if (binding.btnPala.isSelected)
            "Herramienta Pala seleccionada para quitar tierra suelta" else "Seleccionar herramienta Pala"

        binding.btnPincel.contentDescription = if (binding.btnPincel.isSelected)
            "Herramienta Pincel seleccionada para limpiar detalles finos del fósil" else "Seleccionar herramienta Pincel"
    }

    private fun finalizarMecanica() {
        binding.areaExcavacionClick.isClickable = false
    }
}