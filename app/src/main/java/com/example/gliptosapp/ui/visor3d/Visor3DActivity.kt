package com.example.gliptosapp.ui.visor3d

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.gliptosapp.R
import com.example.gliptosapp.databinding.Activity3dBinding
import com.example.gliptosapp.ui.helper.AvisoDialog
import com.example.gliptosapp.ui.settings.appearance.applyAccessibilityPreferences
import dagger.hilt.android.AndroidEntryPoint
import io.github.sceneview.node.ModelNode
import kotlinx.coroutines.launch
import io.github.sceneview.math.Rotation
import io.github.sceneview.math.Position

@AndroidEntryPoint
class Visor3DActivity : AppCompatActivity() { //TODO: El fondo del fósil debe ser de otro color, actualmente es un fondo negro pero no se como fixearlo uwu

    private lateinit var binding: Activity3dBinding
    private var modelNode: ModelNode? = null

    private var rotacionX = 0f
    private var rotacionY = 0f
    private val pasoRotacion = 15f
    private val sensibilidadDrag = 0.2f

    private var distanciaCamara = 4.0f
    private val distanciaMinima = 1.5f
    private val distanciaMaxima = 8f

    private lateinit var gestureDetector: GestureDetector
    private lateinit var scaleGestureDetector: ScaleGestureDetector

    companion object {
        private const val EXTRA_MODEL_PATH = "extra_model_path"
        private const val EXTRA_TITULO = "extra_titulo"

        fun newIntent(context: Context, modelPath: String, titulo: String): Intent {
            return Intent(context, Visor3DActivity::class.java).apply {
                putExtra(EXTRA_MODEL_PATH, modelPath)
                putExtra(EXTRA_TITULO, titulo)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = Activity3dBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.applyAccessibilityPreferences()
        val modelPath = intent.getStringExtra(EXTRA_MODEL_PATH) ?: "models/duck.glb"
        val titulo = intent.getStringExtra(EXTRA_TITULO) ?: ""

        binding.btnBack.setOnClickListener { finish() }
        binding.btnAyuda.setOnClickListener {
            AvisoDialog.mostrar(
                context = this,
                mensaje = getString(R.string.ayuda_visor)
            )
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.visor3d)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.tvTituloFosil.text = titulo

        cargarModelo(modelPath)
        configurarBotones()
        configurarGestos()
    }

    private fun cargarModelo(modelPath: String) {
        lifecycleScope.launch {
            val modelInstance = binding.sceneView.modelLoader.loadModelInstance(modelPath)

            if (modelInstance == null) {
                return@launch
            }

            val node = ModelNode(
                modelInstance = modelInstance,
                scaleToUnits = 1.0f
            ).apply {
                centerOrigin()
            }

            modelNode = node
            binding.sceneView.addChildNode(node)
            binding.sceneView.cameraNode.position = Position(z = distanciaCamara)
        }
    }

    private fun configurarBotones() {
        binding.btnRotarIzq.setOnClickListener {
            ocultarGestoAyuda()
            rotacionY -= pasoRotacion
            aplicarRotacion()
        }
        binding.btnRotarDer.setOnClickListener {
            ocultarGestoAyuda()
            rotacionY += pasoRotacion
            aplicarRotacion()
        }
        binding.btnRotarArriba.setOnClickListener {
            ocultarGestoAyuda()
            rotacionX -= pasoRotacion
            aplicarRotacion()
        }
        binding.btnRotarAbajo.setOnClickListener {
            ocultarGestoAyuda()
            rotacionX += pasoRotacion
            aplicarRotacion()
        }
    }

    private fun configurarGestos() {
        binding.sceneView.cameraManipulator = null

        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onScroll(
                e1: MotionEvent?,
                e2: MotionEvent,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                rotacionY -= distanceX * sensibilidadDrag
                rotacionX -= distanceY * sensibilidadDrag
                aplicarRotacion()
                return true
            }
        })

        scaleGestureDetector = ScaleGestureDetector(
            this,
            object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
                override fun onScale(detector: ScaleGestureDetector): Boolean {
                    distanciaCamara = (distanciaCamara / detector.scaleFactor)
                        .coerceIn(distanciaMinima, distanciaMaxima)
                    binding.sceneView.cameraNode.position = Position(z = distanciaCamara)
                    return true
                }
            })

        binding.sceneView.setOnTouchListener { _, event ->
            ocultarGestoAyuda()
            scaleGestureDetector.onTouchEvent(event)
            gestureDetector.onTouchEvent(event)
            true
        }
    }

    private fun aplicarRotacion() {
        modelNode?.rotation = Rotation(x = rotacionX, y = rotacionY)
    }

    private fun ocultarGestoAyuda() {
        if (binding.lottieGesto.isVisible) {
            binding.lottieGesto.animate()
                .alpha(0f)
                .setDuration(200)
                .withEndAction {
                    binding.lottieGesto.visibility = View.GONE
                    binding.lottieGesto.cancelAnimation()
                }
                .start()
        }
    }
}