package com.example.gliptosapp.ui.excavation

import android.os.Bundle
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
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.gliptosapp.R
import com.example.gliptosapp.databinding.ActivityExcavacionBinding
import com.example.gliptosapp.ui.settings.sound.SoundManager
import com.example.gliptosapp.ui.settings.vibration.VibrationManager
import com.example.gliptosapp.ui.settings.appearance.applyFontScale
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
@AndroidEntryPoint
class ExcavacionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExcavacionBinding
    private val viewModel: ExcavacionViewModel by viewModels()
    private lateinit var herramientas: List<ViewGroup>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExcavacionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fosilId = intent.getIntExtra("FOSIL_ID", 1)
        viewModel.inicializarFosil(fosilId)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.parseColor("#CD4A2C1D")),
            navigationBarStyle = SystemBarStyle.dark(Color.parseColor("#CD4A2C1D"))
        )

        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = false
        windowInsetsController.isAppearanceLightNavigationBars = false

        ViewCompat.setOnApplyWindowInsetsListener(binding.layoutPrincipal) { view, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
            )
            val margen = (8 * resources.displayMetrics.density).toInt()

            view.setPadding(0, 0, 0, 0)

            val paramsBotones = binding.contenedorBotonesSuperiores.layoutParams as ConstraintLayout.LayoutParams
            paramsBotones.topMargin = bars.top + margen
            paramsBotones.leftMargin = bars.left + margen
            paramsBotones.rightMargin = bars.right + margen
            binding.contenedorBotonesSuperiores.layoutParams = paramsBotones

            val paramsHerramientas = binding.contenedorHerramientas.layoutParams as ConstraintLayout.LayoutParams
            paramsHerramientas.bottomMargin = bars.bottom + margen
            paramsHerramientas.rightMargin = bars.right + margen
            binding.contenedorHerramientas.layoutParams = paramsHerramientas

            val paramsKira = binding.contenedorKira.layoutParams as ConstraintLayout.LayoutParams
            paramsKira.bottomMargin = bars.bottom + margen
            paramsKira.leftMargin = bars.left + margen
            binding.contenedorKira.layoutParams = paramsKira

            insets
        }

        herramientas = listOf(
            binding.btnPicoContenedor,
            binding.btnPalaContenedor,
            binding.btnPincelContenedor
        )

        configurarHerramientas()
        configurarBotonesSuperiores()
        restaurarEstadoVisual()

        ViewCompat.setAccessibilityPaneTitle(window.decorView, "Pantalla de Excavación")

        binding.txtIndicacionKira.postDelayed({
            binding.txtIndicacionKira.sendAccessibilityEvent(
                android.view.accessibility.AccessibilityEvent.TYPE_VIEW_FOCUSED
            )
        }, 500)
    }

    private fun restaurarEstadoVisual() {
        val mensajeKira = obtenerMensajePorEstado(viewModel.estadoActual)
        val imagenFosil = obtenerImagenPorEstado(viewModel.estadoActual)

        binding.imgFosilFondo.setImageResource(imagenFosil)
        binding.txtIndicacionKira.text = mensajeKira
        actualizarInformacionFosil(mensajeKira)

        VibrationManager.vibrate(this, 600)

        // Cuando llega al estado final deshabilitamos las herramientas
        if (viewModel.estadoActual == 5) {
            finalizarMecanica()
        }
    }

    private fun finalizarMecanica() {
        herramientas.forEach {
            it.setOnClickListener(null)
            it.isSelected = false
            it.isEnabled = false
            it.contentDescription = "Excavación completada"
        }
        // Anunciamos a TalkBack que terminó
        binding.txtIndicacionKira.announceForAccessibility(
            "¡Felicitaciones! Excavación completada."
        )
    }

    private fun configurarHerramientas() {
        binding.btnPicoContenedor.setOnClickListener { activarHerramienta("PICO", binding.btnPicoContenedor) }
        binding.btnPalaContenedor.setOnClickListener { activarHerramienta("PALA", binding.btnPalaContenedor) }
        binding.btnPincelContenedor.setOnClickListener { activarHerramienta("PINCEL", binding.btnPincelContenedor) }
    }

    private fun activarHerramienta(herramienta: String, botonActivo: ViewGroup) {
        val esCorrecta = verificarHerramientaCorrecta(herramienta)
        if (esCorrecta) {
            selectedToolSound(herramienta)
            herramientas.forEach { it.isSelected = false }
            botonActivo.isSelected = true
            botonActivo.announceForAccessibility("Usando herramienta $herramienta.")
            avanzarProgreso()
        } else {
            SoundManager.playError()
            errorFeedback()
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
        return when (viewModel.estadoActual) {
            1, 2 -> herramienta == "PICO"
            3 -> herramienta == "PALA"
            4 -> herramienta == "PINCEL"
            else -> false
        }
    }

    private fun avanzarProgreso() {
        viewModel.avanzarEstado() // Aquí el ViewModel marca como COMPLETADO si llega a 5
        restaurarEstadoVisual()
    }

    private fun obtenerMensajePorEstado(estado: Int): String {
        val nombreFosil = viewModel.nombreFosilBase.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }
        return when (estado) {
            1 -> "¡Hola! Vamos a excavar. Primero, seleccioná el Pico para romper la tierra dura."
            2 -> "Rompiste la capa superior. ¡Dale otra vez con el Pico!"
            3 -> "Piedras removidas. ¡Cambiá a la Pala para limpiar los escombros!"
            4 -> "¡Uau! Ya se distingue la silueta. ¡Usá el Pincel para limpiar el polvo de los huesos!"
            5 -> "¡Increíble! Desenterraste un $nombreFosil completo. ¡Sos un gran paleontólogo!"
            else -> ""
        }
    }

    private fun obtenerImagenPorEstado(estado: Int): Int {
        val nombreDrawable = "${viewModel.nombreFosilBase}_$estado"
        val resourceId = resources.getIdentifier(nombreDrawable, "drawable", packageName)
        return if (resourceId != 0) resourceId else R.drawable.gliptodonte_1
    }

    private fun actualizarInformacionFosil(mensajeKira: String) {
        val nombreFosil = viewModel.nombreFosilBase.replaceFirstChar { it.uppercase() }
        val descripcionAccesible = "Fósil de $nombreFosil en etapa ${viewModel.estadoActual} de 5. $mensajeKira"
        binding.imgFosilFondo.contentDescription = descripcionAccesible
        binding.imgFosilFondo.announceForAccessibility(mensajeKira)
        actualizarDescripcionesBotones()
    }

    private fun errorFeedback() {
        val herramientaCorrecta = obtenerNombreHerramientaRequerida()
        val avisoError = "¡Uy! Esa no es la herramienta. Tenés que seleccionar el $herramientaCorrecta."
        binding.txtIndicacionKira.text = avisoError
        binding.txtIndicacionKira.announceForAccessibility(avisoError)
    }

    private fun obtenerNombreHerramientaRequerida(): String {
        return when (viewModel.estadoActual) {
            1, 2 -> "Pico"
            3 -> "Pala"
            4 -> "Pincel"
            else -> ""
        }
    }

    private fun actualizarDescripcionesBotones() {
        binding.btnPicoContenedor.contentDescription = "Herramienta Pico"
        binding.btnPalaContenedor.contentDescription = "Herramienta Pala"
        binding.btnPincelContenedor.contentDescription = "Herramienta Pincel"
    }

    private fun configurarBotonesSuperiores() {
        binding.btnBack.setOnClickListener { finish() }

        binding.btnInfo.setOnClickListener {
            val nombreFosil = viewModel.nombreFosilBase.replaceFirstChar { it.uppercase() }
            mostrarDialogo(
                "¿Cómo jugar?",
                "¡Ayudá a Kira a desenterrar el fósil del $nombreFosil! " +
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
}