package com.example.gliptosapp.ui.excavation

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.constraintlayout.widget.ConstraintLayout
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

        // Para que dibuje la pantalla
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Ocultamos las barras
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

        // Recupero el tamaño del statusBars y de navigationBars
        ViewCompat.setOnApplyWindowInsetsListener(binding.layoutPrincipal) { _, insets ->

            val topBarHeight = insets.getInsetsIgnoringVisibility(WindowInsetsCompat.Type.statusBars()).top
            val bottomBarHeight = insets.getInsetsIgnoringVisibility(WindowInsetsCompat.Type.navigationBars()).bottom

            // Esto es para posicionar el contenedor de kira por debajo de donde estaria el statusBar
            val paramsKira = binding.contenedorKira.layoutParams as ConstraintLayout.LayoutParams
            paramsKira.topMargin = topBarHeight
            binding.contenedorKira.layoutParams = paramsKira

            // // Esto es para posicionar el contenedor de herramientas por debajo de donde estaria el navigationBar
            val paramsHerramientas = binding.contenedorHerramientas.layoutParams as ConstraintLayout.LayoutParams
            paramsHerramientas.bottomMargin = bottomBarHeight
            binding.contenedorHerramientas.layoutParams = paramsHerramientas

            insets
        }

        // Métodos de inicialización del juego
        configurarHerramientas()
        configurarMecanicaExcavacion()
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

        botones.forEach { it.isSelected = false }
        botonActivo.isSelected = true

        actualizarDescripcionesAccesibles()
        botonActivo.announceForAccessibility("Herramienta $herramienta lista para usar.")
    }

    private fun configurarMecanicaExcavacion() {
        binding.areaExcavacionClick.setOnClickListener {
            valzarProgreso()
        }
    }

    private fun valzarProgreso() {
        when (estadoActual) {
            1 -> {
                if (herramientaSeleccionada == "PICO") {
                    actualizarEstado(R.drawable.gliptodonte_2, "¡Crack! Rompiste la capa superior. Dale otra vez con el Pico.")
                } else { errorFeedback() }
            }
            2 -> {
                if (herramientaSeleccionada == "PICO") {
                    actualizarEstado(R.drawable.gliptodonte_3, "Piedras removidas. Cambiá a la Pala para limpiar los escombros.")
                } else { errorFeedback() }
            }
            3 -> {
                if (herramientaSeleccionada == "PALA") {
                    actualizarEstado(R.drawable.gliptodonte_4, "¡Uau! Ya se distingue la silueta. Usá el Pincel para limpiar el polvo de los huesos.")
                } else { errorFeedback() }
            }
            4 -> {
                if (herramientaSeleccionada == "PINCEL") {
                    actualizarEstado(R.drawable.gliptodonte_5, "¡Increíble! Desenterraste un Gliptodonte completo. ¡Sos un gran paleontólogo!")
                    finalizarMecanica()
                } else { errorFeedback() }
            }
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