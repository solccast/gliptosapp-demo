package com.example.gliptosapp.ui.excavation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.activity.SystemBarStyle
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.gliptosapp.R
import com.example.gliptosapp.databinding.ActivityExcavacionBinding
import com.example.gliptosapp.ui.MainActivity
import com.example.gliptosapp.ui.helper.AvisoDialog
import com.example.gliptosapp.ui.helper.KiraNarration
import com.example.gliptosapp.ui.helper.SesionApp
import com.example.gliptosapp.ui.settings.appearance.applyAccessibilityPreferences
import com.example.gliptosapp.ui.settings.sound.SoundManager
import com.example.gliptosapp.ui.settings.vibration.VibrationManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class ExcavacionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExcavacionBinding
    private val viewModel: ExcavacionViewModel by viewModels()
    private lateinit var herramientas: List<ViewGroup>
    @Inject
    lateinit var kiraNarration: KiraNarration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExcavacionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.applyAccessibilityPreferences()

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

            val paramsAcciones = binding.contenedorAccionesCompletado.layoutParams as ConstraintLayout.LayoutParams
            paramsAcciones.bottomMargin = bars.bottom + margen
            paramsAcciones.leftMargin = bars.left + margen
            paramsAcciones.rightMargin = bars.right + margen
            binding.contenedorAccionesCompletado.layoutParams = paramsAcciones

            val paramsKira = binding.contenedorKira.layoutParams as ConstraintLayout.LayoutParams
            paramsKira.bottomMargin = bars.bottom + margen
            paramsKira.leftMargin = bars.left + margen
            binding.contenedorKira.layoutParams = paramsKira

            insets
        }


        // Actualizamos los IDs a los nuevos contenedores
        herramientas = listOf(
            binding.btnPicoContenedor,
            binding.btnPalaContenedor,
            binding.btnPincelContenedor
        )

        configurarHerramientas()
        configurarBotonesSuperiores()
        configurarBotonesCompletado()
        observarEstado()

        ViewCompat.setAccessibilityPaneTitle(window.decorView, "Pantalla de Excavación")

        binding.txtIndicacionKira.postDelayed({
            binding.txtIndicacionKira.sendAccessibilityEvent(
                android.view.accessibility.AccessibilityEvent.TYPE_VIEW_FOCUSED
            )
        }, 500)

        if (!SesionApp.infoExcavacionMostrada) {
            SesionApp.infoExcavacionMostrada = true
            mostrarInfoExcavacion()
        }
    }

    private fun observarEstado() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.estado.collect { estadoJuego ->
                    if (estadoJuego.cargado) {
                        pintarEstado(estadoJuego.estadoActual, estadoJuego.yaCompletado)
                    }
                }
            }
        }
    }

    private fun pintarEstado(estado: Int, completado: Boolean) {
        val estadoParaMostrar = if (completado) 5 else estado
        val mensajeKira = obtenerMensajePorEstado(estadoParaMostrar)
        val imagenFosil = obtenerImagenPorEstado(estadoParaMostrar)

        binding.imgFosilFondo.setImageResource(imagenFosil)
        binding.txtIndicacionKira.text = mensajeKira

        kiraNarration.speak( this, mensajeKira )
        actualizarInformacionFosil(mensajeKira, estadoParaMostrar)

        if (completado) mostrarModoCompletado() else mostrarModoJuego()
    }

    private fun mostrarModoCompletado() {
        binding.contenedorHerramientas.visibility = View.GONE
        binding.contenedorAccionesCompletado.visibility = View.VISIBLE
        herramientas.forEach {
            it.isEnabled = false
            it.isSelected = false
        }
    }

    private fun mostrarModoJuego() {
        binding.contenedorAccionesCompletado.visibility = View.GONE
        binding.contenedorHerramientas.visibility = View.VISIBLE
        herramientas.forEach { it.isEnabled = true }
        actualizarDescripcionesBotones()
    }

    private fun configurarHerramientas() {
        binding.btnPicoContenedor.setOnClickListener { activarHerramienta("PICO", binding.btnPicoContenedor) }
        binding.btnPalaContenedor.setOnClickListener { activarHerramienta("PALA", binding.btnPalaContenedor) }
        binding.btnPincelContenedor.setOnClickListener { activarHerramienta("PINCEL", binding.btnPincelContenedor) }
    }

    private fun configurarBotonesCompletado() {
        binding.btnVerColeccion.setOnClickListener { irAColeccion() }
        binding.btnVolverAExcavar.setOnClickListener { viewModel.reiniciarJuego() }
    }

    private fun irAColeccion() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("DESTINO_NAV", "colectionFragment")
        }
        startActivity(intent)
        finish()
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
        return when (viewModel.estado.value.estadoActual) {
            1, 2 -> herramienta == "PICO"
            3 -> herramienta == "PALA"
            4 -> herramienta == "PINCEL"
            else -> false
        }
    }

    private fun avanzarProgreso() {
        val completadoAntes = viewModel.estado.value.yaCompletado
        viewModel.avanzarEstado()
        VibrationManager.vibrate(this, 600)

        if (!completadoAntes && viewModel.estado.value.yaCompletado) {
            binding.txtIndicacionKira.announceForAccessibility(
                "¡Felicitaciones! Excavación completada."
            )
        }
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

    private fun actualizarInformacionFosil(mensajeKira: String, estado: Int) {
        val nombreFosil = viewModel.nombreFosilBase.replaceFirstChar { it.uppercase() }
        val descripcionAccesible = "Fósil de $nombreFosil en etapa $estado de 5. $mensajeKira"
        binding.imgFosilFondo.contentDescription = descripcionAccesible
        binding.imgFosilFondo.announceForAccessibility(mensajeKira)
    }

    private fun errorFeedback() {
        val herramientaCorrecta = obtenerNombreHerramientaRequerida()
        val avisoError = "¡Uy! Esa no es la herramienta. Tenés que seleccionar el $herramientaCorrecta."
        kiraNarration.speak(this, avisoError)
        binding.txtIndicacionKira.text = avisoError
        binding.txtIndicacionKira.announceForAccessibility(avisoError)
    }

    private fun obtenerNombreHerramientaRequerida(): String {
        return when (viewModel.estado.value.estadoActual) {
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
            mostrarInfoExcavacion()
        }
    }
    private fun mostrarInfoExcavacion() {
        AvisoDialog.mostrar(this, getString(R.string.ayuda_excavacion))
    }

}