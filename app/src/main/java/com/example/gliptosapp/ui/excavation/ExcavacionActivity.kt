package com.example.gliptosapp.ui.excavation

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
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

        // SOLUCIÓN: Cambiamos la propiedad isSelected de los botones.
        // Esto gatilla de manera automática los Selectores XML de fondo y de ícono (tint).
        botones.forEach { it.isSelected = false }
        botonActivo.isSelected = true

        // Actualizamos los textos descriptivos para que TalkBack sepa cuál está seleccionado
        actualizarDescripcionesAccesibles()

        // Anuncio inmediato para usuarios con discapacidad visual
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
        // Cambia el fondo general de la pantalla
        binding.imgFosilFondo.setImageResource(resourceImg)
        binding.txtIndicacionKira.text = mensajeKira

        // Accesibilidad: El área de juego describe lo que se ve en el fondo
        binding.areaExcavacionClick.contentDescription = "Fósil en etapa $estadoActual de 5. $mensajeKira"
        binding.areaExcavacionClick.announceForAccessibility(mensajeKira)
    }

    private fun errorFeedback() {
        val avisoError = "¡Huy! Esa herramienta no sirve acá. Escuchá la indicación de Kira arriba."
        binding.areaExcavacionClick.announceForAccessibility(avisoError)
    }

    private fun actualizarDescripcionesAccesibles() {
        // Modificamos el contentDescription dinámicamente según el estado de selección
        binding.btnPico.contentDescription = if (binding.btnPico.isSelected)
            "Herramienta Pico seleccionada para romper roca" else "Seleccionar herramienta Pico"

        binding.btnPala.contentDescription = if (binding.btnPala.isSelected)
            "Herramienta Pala seleccionada para quitar tierra suelta" else "Seleccionar herramienta Pala"

        binding.btnPincel.contentDescription = if (binding.btnPincel.isSelected)
            "Herramienta Pincel seleccionada para limpiar detalles finos del fósil" else "Seleccionar herramienta Pincel"
    }

    private fun finalizarMecanica() {
        binding.areaExcavacionClick.isClickable = false
        // El juego ha terminado con éxito. Podés agregar la navegación de regreso o un diálogo de premio.
    }
}