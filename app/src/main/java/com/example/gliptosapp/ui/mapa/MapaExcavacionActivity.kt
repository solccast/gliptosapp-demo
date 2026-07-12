package com.example.gliptosapp.ui.mapa

import android.view.accessibility.AccessibilityManager
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.views.overlay.MapEventsOverlay
import androidx.activity.enableEdgeToEdge
import androidx.activity.SystemBarStyle
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowCompat
import android.graphics.Color
import androidx.constraintlayout.widget.ConstraintLayout
import android.content.Intent
import androidx.core.content.ContextCompat
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.util.MapTileIndex
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.osmdroid.config.Configuration
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import android.widget.Button
import android.preference.PreferenceManager
import android.view.View
import android.widget.ImageButton
import com.example.gliptosapp.R
import com.example.gliptosapp.ui.excavation.ExcavacionActivity
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.events.MapListener
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
class MapaExcavacionActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private val marcadores = mutableListOf<Marker>()
    private lateinit var accessibilityHelper: MapaAccesibleHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.parseColor("#CD4A2C1D")),
            navigationBarStyle = SystemBarStyle.dark(Color.parseColor("#CD4A2C1D"))
        )

        Configuration.getInstance().load(
            applicationContext,
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )

        setContentView(R.layout.activity_mapa_excavacion)

        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = false
        windowInsetsController.isAppearanceLightNavigationBars = false

        val layoutPrincipal = findViewById<ConstraintLayout>(R.id.layoutPrincipalMapa)
        mapView = findViewById(R.id.mapView)
        val btnVolver = findViewById<ImageButton>(R.id.btnVolver)
        val btnZoomIn = findViewById<ImageButton>(R.id.btnZoomIn)
        val btnZoomOut = findViewById<ImageButton>(R.id.btnZoomOut)
        val btnAyuda = findViewById<ImageButton>(R.id.btnAyuda)

        ViewCompat.setOnApplyWindowInsetsListener(layoutPrincipal) { view, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
            )
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        val viewAccesibilidad = findViewById<View>(R.id.viewAccesibilidad)
        accessibilityHelper = MapaAccesibleHelper(viewAccesibilidad, mapView)
        ViewCompat.setAccessibilityDelegate(viewAccesibilidad, accessibilityHelper)

        configurarMapa()
        agregarMarcadorExcavacion(GeoPoint(-34.9205, -57.9536))
        agregarMarcadorExcavacion(GeoPoint(-34.9150, -57.9480))
        agregarOverlayDebug()
        // Agregá todos los que quieras sin impacto de performance
        configurarTapEnZonaVacia()

        // TalkBack: anunciar instrucciones al entrar
        mapView.post {
            mostrarInstruccionesIniciales()
        }

        btnVolver.setOnClickListener { finish() }

        // Zoom accesible
        btnZoomIn.setOnClickListener {
            mapView.controller.zoomIn()
            mapView.announceForAccessibility("Zoom nivel ${mapView.zoomLevelDouble.toInt()}")
        }

        btnZoomOut.setOnClickListener {
            mapView.controller.zoomOut()
            mapView.announceForAccessibility("Zoom nivel ${mapView.zoomLevelDouble.toInt()}")
        }

        // Botón de ayuda: re-anuncia instrucciones
        btnAyuda.setOnClickListener {
            mostrarInstruccionesIniciales()
        }
    }

    private fun isTalkBackActivo(): Boolean {
        val am = getSystemService(ACCESSIBILITY_SERVICE) as AccessibilityManager
        return am.isEnabled && am.isTouchExplorationEnabled
    }

    private fun mostrarInstruccionesIniciales() {
        if (isTalkBackActivo()) {
            mapView.announceForAccessibility(
                "Mapa de La Plata. Explorá la pantalla para encontrar " +
                        "zonas de excavación marcadas con chinches rojas. " +
                        "Cuando encuentres una, tocá dos veces para excavar. " +
                        "Usá los botones de acercar y alejar para navegar el mapa."
            )
        }
    }

    private fun feedbackZonaVacia() {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(VIBRATOR_SERVICE) as Vibrator
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(80, VibrationEffect.DEFAULT_AMPLITUDE)
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(150)
        }

        mapView.announceForAccessibility(
            "En esta zona no se encontraron fósiles. " +
                    "Intentá explorar hacia el centro de la ciudad."
        )
    }

    private fun configurarTapEnZonaVacia() {
        val receptor = object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                val tapCercaDeMarcador = marcadores.any { marcador ->
                    val pixelMarcador = mapView.projection.toPixels(marcador.position, null)
                    val pixelTap = mapView.projection.toPixels(p, null)
                    val distancia = Math.hypot(
                        (pixelMarcador.x - pixelTap.x).toDouble(),
                        (pixelMarcador.y - pixelTap.y).toDouble()
                    )
                    distancia < radioEnPixeles()
                }

                if (!tapCercaDeMarcador) {
                    feedbackZonaVacia()
                }

                return true
            }

            override fun longPressHelper(p: GeoPoint): Boolean = false
        }

        mapView.overlays.add(0, MapEventsOverlay(receptor))
    }

    // Radio dinámico según zoom
    private fun radioEnPixeles(): Float {
        return when {
            mapView.zoomLevelDouble >= 15 -> 60f
            mapView.zoomLevelDouble >= 13 -> 80f
            else -> 100f
        }
    }


    private fun configurarMapa() {
        mapView.setMultiTouchControls(true)
        // Deshabilitamos los botones nativos de osmdroid
        // porque no son accesibles para TalkBack
        mapView.zoomController.setVisibility(
            org.osmdroid.views.CustomZoomButtonsController.Visibility.NEVER
        )

        val mapController = mapView.controller

        val mapaWatercolor = object : OnlineTileSourceBase(
            "StadiaStamenWatercolor", 1, 16, 256, ".jpg",
            arrayOf("https://tiles.stadiamaps.com/tiles/stamen_watercolor/")
        ) {
            override fun getTileURLString(pMapTileIndex: Long): String {
                val apiKey = "ffacce07-a9c2-4d8a-9f4a-6e6cb7598fac"
                return baseUrl +
                        MapTileIndex.getZoom(pMapTileIndex) + "/" +
                        MapTileIndex.getX(pMapTileIndex) + "/" +
                        MapTileIndex.getY(pMapTileIndex) +
                        mImageFilenameEnding + "?api_key=" + apiKey
            }
        }

        mapView.setTileSource(mapaWatercolor)
        mapController.setZoom(14.0)
        mapController.setCenter(GeoPoint(-34.9205, -57.9536))
        mapView.setScrollableAreaLimitDouble(BoundingBox(-34.88, -57.90, -34.97, -58.00))
        mapView.setMinZoomLevel(12.0)
        mapView.setMaxZoomLevel(16.0)

        // Actualizar nodos accesibles cuando el mapa se mueve
        mapView.addMapListener(object : MapListener {
            override fun onScroll(event: ScrollEvent): Boolean {
                accessibilityHelper.actualizarPosiciones()
                return false
            }
            override fun onZoom(event: ZoomEvent): Boolean {
                accessibilityHelper.actualizarPosiciones()
                return false
            }
        })
    }

    private fun agregarMarcadorExcavacion(punto: GeoPoint) {
        // Marcador visual (igual que antes)
        val marcador = Marker(mapView)
        marcador.position = punto
        marcador.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
        marcador.icon = ContextCompat.getDrawable(mapView.context, R.drawable.ic_gliptodonte)
        marcador.setOnMarkerClickListener { _, _ ->
            if (!isTalkBackActivo()) navegarAExcavacion()
            true
        }
        marcadores.add(marcador)
        mapView.overlays.add(marcador)
        mapView.invalidate()

        // Nodo virtual para TalkBack (sin View real)
        accessibilityHelper.agregarMarcador(
            position = punto,
            descripcion = "Zona de excavación. Fósil de Gliptodonte oculto.",
            onActivar = { navegarAExcavacion() }
        )
    }

    private fun agregarOverlayDebug() {
        val overlayDebug = object : org.osmdroid.views.overlay.Overlay() {
            override fun draw(
                canvas: android.graphics.Canvas,
                mapView: MapView,
                shadow: Boolean
            ) {
                if (shadow) return
                val paint = android.graphics.Paint().apply {
                    color = android.graphics.Color.argb(120, 0, 200, 0)
                    style = android.graphics.Paint.Style.FILL
                }
                val paintBorde = android.graphics.Paint().apply {
                    color = android.graphics.Color.GREEN
                    style = android.graphics.Paint.Style.STROKE
                    strokeWidth = 3f
                }
                marcadores.forEach { marcador ->
                    val pixel = mapView.projection.toPixels(marcador.position, null)
                    val radio = 54 * resources.displayMetrics.density
                    canvas.drawCircle(pixel.x.toFloat(), pixel.y.toFloat(), radio, paint)
                    canvas.drawCircle(pixel.x.toFloat(), pixel.y.toFloat(), radio, paintBorde)
                }
            }
        }
        mapView.overlays.add(overlayDebug)
    }

    private fun navegarAExcavacion() {
        val intent = Intent(this, ExcavacionActivity::class.java)
        startActivity(intent)
    }

    override fun onResume() { super.onResume(); mapView.onResume() }
    override fun onPause() { super.onPause(); mapView.onPause() }
}