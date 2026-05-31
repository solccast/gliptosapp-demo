package com.example.gliptosapp.ui.ra

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gliptosapp.R
import com.example.gliptosapp.databinding.ActivityRaBinding
import com.google.ar.core.Config
import com.google.ar.core.Frame
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.core.Session
import dagger.hilt.android.AndroidEntryPoint
import io.github.sceneview.ar.ARSceneView
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.node.ModelNode

@AndroidEntryPoint
class RAFosilActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRaBinding

    // Guardamos el frame actual para hacer hitTest al tocar
    private var currentFrame: Frame? = null
    private var anchorNode: AnchorNode? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRaBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rafosile)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupAR()
    }

    private fun setupAR() {
        binding.arSceneView.apply {

            // ✅ Si ARCore no está disponible, cerramos la pantalla AR sin crashear
            onSessionFailed = { exception ->
                // Podés mostrar un mensaje o simplemente cerrar la activity
                Toast.makeText(
                    this@RAFosilActivity,
                    "Este dispositivo no soporta Realidad Aumentada",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }

            sessionConfiguration = { session, config ->
                config.lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR
            }

            // Planeador de superficies visible
            planeRenderer.isEnabled = true
            planeRenderer.isVisible = true

            // Recibimos el frame de cada actualización de ARCore
            onSessionUpdated = { _: Session, frame: Frame ->
                currentFrame = frame
            }

            // Touch: hacemos hitTest con el frame actual
            setOnTouchListener { _, motionEvent: MotionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_UP) {
                    placeModel(binding.arSceneView, motionEvent)
                }
                true
            }
        }
    }

    private fun placeModel(sceneView: ARSceneView, motionEvent: MotionEvent) {
        val frame = currentFrame ?: return

        val hitResults: List<HitResult> = frame.hitTest(motionEvent)

        val hit = hitResults.firstOrNull { hitResult ->
            val trackable = hitResult.trackable
            trackable is Plane &&
                    trackable.isPoseInPolygon(hitResult.hitPose) &&
                    trackable.type == Plane.Type.HORIZONTAL_UPWARD_FACING
        } ?: return

        // Removemos el nodo anterior
        anchorNode?.let { sceneView.removeChildNode(it) }

        val anchor = hit.createAnchor()

        anchorNode = AnchorNode(sceneView.engine, anchor).also { node ->

            // ✅ Correcto: agregar el nodo a la sceneView así
            sceneView.addChildNode(node)

            ModelNode(
                modelInstance = sceneView.modelLoader.createModelInstance("models/duck.glb"),
                scaleToUnits = 0.3f
            ).apply {
                parent = node   // el ModelNode sí usa parent = node (otro Node)
                isEditable = true
            }

            binding.tvInstruccion.visibility = View.GONE
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        binding.arSceneView.destroy()
    }
}