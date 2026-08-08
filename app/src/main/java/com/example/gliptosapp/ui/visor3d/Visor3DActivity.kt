package com.example.gliptosapp.ui.visor3d

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.gliptosapp.R
import com.example.gliptosapp.databinding.Activity3dBinding
import com.google.android.filament.View
import dagger.hilt.android.AndroidEntryPoint
import io.github.sceneview.node.ModelNode
import kotlinx.coroutines.launch
import io.github.sceneview.math.Rotation
import io.github.sceneview.math.Position
@AndroidEntryPoint
class Visor3DActivity: AppCompatActivity() {

    private lateinit var binding: Activity3dBinding
    private var modelNode: ModelNode? = null
    private var rotacionX = 0f
    private var rotacionY = 0f
    private val pasoRotacion = 15f

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

        val modelPath = intent.getStringExtra(EXTRA_MODEL_PATH) ?: "models/duck.glb"
        val titulo = intent.getStringExtra(EXTRA_TITULO) ?: ""

        binding.btnBack.setOnClickListener {
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.visor3d)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.tvTituloFosil.text = titulo

        cargarModelo(modelPath)
        configurarBotones()
    }

    private fun cargarModelo(modelPath: String) {
        lifecycleScope.launch {
            val modelInstance = binding.sceneView.modelLoader.loadModelInstance(modelPath)

            if (modelInstance == null) {
                // El archivo no existe o no pudo cargarse
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
            binding.sceneView.cameraNode.position = Position(z = 4.0f)
        }
    }

    private fun configurarBotones() {

        binding.btnRotarIzq.setOnClickListener {
            rotacionY -= pasoRotacion
            aplicarRotacion()
        }

        binding.btnRotarDer.setOnClickListener {
            rotacionY += pasoRotacion
            aplicarRotacion()
        }

        binding.btnRotarArriba.setOnClickListener {
            rotacionX -= pasoRotacion
            aplicarRotacion()
        }

        binding.btnRotarAbajo.setOnClickListener {
            rotacionX += pasoRotacion
            aplicarRotacion()
        }
    }

    private fun aplicarRotacion() {
        modelNode?.rotation = Rotation(x = rotacionX, y = rotacionY)
    }
}