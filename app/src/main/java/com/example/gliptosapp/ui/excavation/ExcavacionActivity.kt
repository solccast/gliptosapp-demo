package com.example.gliptosapp.ui.excavation

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.activity.SystemBarStyle
import android.graphics.Color
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import com.example.gliptosapp.R
import com.example.gliptosapp.databinding.ActivityExcavacionBinding
import com.example.gliptosapp.ui.settings.SoundManager
import com.example.gliptosapp.ui.settings.applyFontScale

class ExcavacionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExcavacionBinding
    private var estadoActual = 1
    private lateinit var herramientas: List<ImageButton>

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

        herramientas = listOf(binding.btnPico, binding.btnPala, binding.btnPincel)

        // Configuración del juego
        configurarHerramientas()
        configurarBotonesSuperiores()
        //actualizarDescripcionesAccesibles()
        actualizarInformacionFosil("¡Hola! Vamos a excavar. Primero, seleccioná el Pico para romper la tierra dura.")
    }

    private fun configurarHerramientas() {
        binding.btnPico.setOnClickListener { activarHerramienta("PICO", binding.btnPico) }
        binding.btnPala.setOnClickListener { activarHerramienta("PALA", binding.btnPala) }
        binding.btnPincel.setOnClickListener { activarHerramienta("PINCEL", binding.btnPincel) }
    }

    private fun activarHerramienta(herramienta: String, botonActivo: ImageButton) {
        val esCorrecta = verificarHerramientaCorrecta(herramienta)
        if (esCorrecta) {
            selectedToolSound(herramienta)
            herramientas.forEach { it.isSelected = false }
            botonActivo.isSelected = true
            botonActivo.announceForAccessibility("Usando herramienta $herramienta.")
            avanzarProgreso()
        } else {
            // Pasamos la herramienta intentada para armar un mensaje dinámico y educativo
            SoundManager.playError()
            errorFeedback(herramienta)
        }
    }

    private fun selectedToolSound(herramienta: String) {
        when (herramienta) {
            "PICO" -> SoundManager.playPico()
            "PALA" -> SoundManager.playPala()
            "PINCEL" -> SoundManager.playPincel()
        }
    }

    private fun verificarHerramientaCorrecta(herramienta: String): Boolean {
        return when (estadoActual) {
            1, 2 -> herramienta == "PICO"
            3 -> herramienta == "PALA"
            4 -> herramienta == "PINCEL"
            else -> false
        }
    }

    private fun avanzarProgreso() {
        when (estadoActual) {
            1 -> actualizarEstado(R.drawable.gliptodonte_2, "Rompiste la capa superior. ¡Dale otra vez con el Pico!")
            2 -> actualizarEstado(R.drawable.gliptodonte_3, "Piedras removidas. ¡Cambiá a la Pala para limpiar los escombros!")
            3 -> actualizarEstado(R.drawable.gliptodonte_4, "¡Uau! Ya se distingue la silueta. ¡Usá el Pincel para limpiar el polvo de los huesos!")
            4 -> {
                actualizarEstado(R.drawable.gliptodonte_5, "¡Increíble! Desenterraste un Gliptodonte completo. ¡Sos un gran paleontólogo!")
                finalizarMecanica()
            }
        }
    }

    private fun actualizarEstado(resourceImg: Int, mensajeKira: String) {
        estadoActual++
        binding.imgFosilFondo.setImageResource(resourceImg)
        binding.txtIndicacionKira.text = mensajeKira

        actualizarInformacionFosil(mensajeKira)
    }

    private fun actualizarInformacionFosil(mensajeKira: String) {
        val descripcionAccesible = "Fósil en etapa $estadoActual de 5. $mensajeKira"
        binding.imgFosilFondo.contentDescription = descripcionAccesible

        binding.imgFosilFondo.announceForAccessibility(mensajeKira)
        actualizarDescripcionesBotones()
    }

    private fun errorFeedback(herramientaIntentada: String) {
        val herramientaCorrecta = obtenerNombreHerramientaRequerida()
        val avisoError = "¡Uy! Esa no es la herramienta. Tenés que seleccionar la herramienta $herramientaCorrecta."
        //Feedback para el niño que no usa el talkback
        binding.txtIndicacionKira.text = avisoError
        binding.txtIndicacionKira.announceForAccessibility(avisoError)
    }

    private fun obtenerNombreHerramientaRequerida(): String {
        return when (estadoActual) {
            1, 2 -> "Pico"
            3 -> "Pala"
            4 -> "Pincel"
            else -> ""
        }
    }

    private fun actualizarDescripcionesBotones() {
        binding.btnPico.contentDescription = "Herramienta Pico"

        binding.btnPala.contentDescription = "Herramienta Pala"

        binding.btnPincel.contentDescription = "Herramienta Pincel"
    }

    private fun finalizarMecanica() {
        herramientas.forEach {
            it.setOnClickListener(null)
            it.isSelected = false
            it.contentDescription = "Juego completado"
        }
    }

    //Botones superiores
    private fun configurarBotonesSuperiores() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnInfo.setOnClickListener {
            mostrarDialogo(
                "¿Cómo jugar?",
                "¡Ayudá a Kira a desenterrar el fósil del Gliptodonte! " +
                        "Escuchá o leé con atención su pista. Abajo vas a encontrar tres herramientas: " +
                        "el Pico (para romper la tierra dura y piedras), la Pala (para limpiar los escombros sueltos) " +
                        "y el Pincel (para limpiar el polvo de los huesos). ¡Tocá la herramienta correcta para avanzar paso a paso!"
            )
        }

    }

    private fun mostrarDialogo(titulo: String, mensaje: String) {

        val view = layoutInflater.inflate(R.layout.dialog_ayuda, null)

        val txtAyuda = view.findViewById<TextView>(R.id.txtAyuda)
        txtAyuda.text = mensaje
        (view as? ViewGroup)?.applyFontScale()
        AlertDialog.Builder(this)
            .setTitle(titulo)
            .setView(view)
            .setPositiveButton("¡Entendido!", null)
            .show()
    }

    override fun onResume() {
        super.onResume()
        SoundManager.refreshAudioState(this)
    }

    override fun onPause() {
        super.onPause()
        SoundManager.pauseBackgroundMusic()
    }
}