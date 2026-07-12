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
import android.widget.ImageButton
import com.example.gliptosapp.R
import com.example.gliptosapp.ui.excavation.ExcavacionActivity

class MapaExcavacionActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private val marcadores = mutableListOf<Marker>()
    private var ultimoMarcadorAnunciado: Marker? = null

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

        configurarMapa()
        agregarMarcadorExcavacion(GeoPoint(-34.9205, -57.9536))
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
                    distancia < 80
                }

                if (!tapCercaDeMarcador) {
                    ultimoMarcadorAnunciado = null
                    mapView.announceForAccessibility(
                        "En esta zona no se encontraron fósiles. " +
                                "Intentá explorar hacia el centro de la ciudad."
                    )
                }
                return true
            }

            override fun longPressHelper(p: GeoPoint): Boolean = false
        }

        mapView.overlays.add(0, MapEventsOverlay(receptor))
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
    }

    private fun agregarMarcadorExcavacion(punto: GeoPoint) {
        val marcador = Marker(mapView)
        marcador.position = punto
        marcador.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
        marcador.title = "Punto de excavación"
        marcador.subDescription = "Fósil de Gliptodonte oculto"
        marcador.icon = ContextCompat.getDrawable(mapView.context, R.drawable.ic_gliptodonte)

        marcador.setOnMarkerClickListener { m, _ ->
            if (isTalkBackActivo()) {
                if (ultimoMarcadorAnunciado == m) {
                    // Segunda interacción → navegar
                    navegarAExcavacion()
                } else {
                    // Primera interacción → anunciar
                    ultimoMarcadorAnunciado = m
                    mapView.announceForAccessibility(
                        "Zona de excavación encontrada. Tocá dos veces para inspeccionar."
                    )
                }
            } else {
                navegarAExcavacion()
            }
            true
        }

        marcadores.add(marcador)
        mapView.overlays.add(marcador)
        mapView.invalidate()
    }

    private fun navegarAExcavacion() {
        val intent = Intent(this, ExcavacionActivity::class.java)
        startActivity(intent)
    }

    override fun onResume() { super.onResume(); mapView.onResume() }
    override fun onPause() { super.onPause(); mapView.onPause() }
}