package com.example.gliptosapp.ui.mapa

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Configuración de pantalla completa (Edge-to-Edge)
        // Se debe llamar ANTES de setContentView
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.parseColor("#CD4A2C1D")),
            navigationBarStyle = SystemBarStyle.dark(Color.parseColor("#CD4A2C1D"))
        )

        // 2. Inicializar osmdroid
        Configuration.getInstance().load(
            applicationContext,
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )

        setContentView(R.layout.activity_mapa_excavacion)

        // 3. Control de apariencia de las barras (iconos claros/oscuros)
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = false
        windowInsetsController.isAppearanceLightNavigationBars = false

        // Referencias
        val layoutPrincipal = findViewById<ConstraintLayout>(R.id.layoutPrincipalMapa)
        mapView = findViewById(R.id.mapView)
        val btnVolver = findViewById<ImageButton>(R.id.btnVolver)

        // 4. Lógica de Insets (La clave para evitar que el navbar tape el mapa)
        ViewCompat.setOnApplyWindowInsetsListener(layoutPrincipal) { view, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
            )
            // Aplicamos padding a la raíz: esto mueve todo el contenido a la zona segura
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        configurarMapa()
        agregarMarcadorExcavacion(GeoPoint(-34.9205, -57.9536))

        btnVolver.setOnClickListener {
            finish()
        }
    }

    /*
    private fun configurarMapa() {
        mapView.setMultiTouchControls(true)
        val mapController = mapView.controller

        // 1. CONFIGURACIÓN DEL MAPA LIMPIO (Sin comercios, ideal para niños)
        // Usamos el servidor de CARTO Voyager, que resalta calles e instituciones sin ruido visual.
        val mapaLimpioTileSource = XYTileSource(
            "CartoVoyager",
            1,
            20, // Zoom máximo
            256,
            ".png",
            arrayOf(
                "https://a.basemaps.cartocdn.com/rastertiles/voyager/",
                "https://b.basemaps.cartocdn.com/rastertiles/voyager/",
                "https://c.basemaps.cartocdn.com/rastertiles/voyager/"
            )
        )
        mapView.setTileSource(mapaLimpioTileSource)

        // Nivel de zoom y centro
        mapController.setZoom(16.0)
        val laPlataCenter = GeoPoint(-34.9205, -57.9536)
        mapController.setCenter(laPlataCenter)

        // Límites para evitar que se pierdan
        val limitesLaPlata = BoundingBox(-34.88, -57.90, -34.97, -58.00)
        mapView.setScrollableAreaLimitDouble(limitesLaPlata)
        mapView.setMinZoomLevel(12.0)
        mapView.setMaxZoomLevel(20.0)
    }
    */


    private fun configurarMapa() {
        mapView.setMultiTouchControls(true)
        val mapController = mapView.controller

        // 1. EL PROVEEDOR PERSONALIZADO PARA STAMEN WATERCOLOR
        val mapaWatercolor = object : OnlineTileSourceBase(
            "StadiaStamenWatercolor",
            1,  // Nivel de zoom mínimo
            16, // Nivel de zoom máximo (¡Crítico para Acuarela!)
            256,
            ".jpg", // Formato requerido por Stadia para Acuarela
            arrayOf("https://tiles.stadiamaps.com/tiles/stamen_watercolor/")
        ) {
            override fun getTileURLString(pMapTileIndex: Long): String {
                // Aquí construimos la URL e INYECTAMOS LA API KEY
                val apiKey = "ffacce07-a9c2-4d8a-9f4a-6e6cb7598fac"

                return baseUrl +
                        MapTileIndex.getZoom(pMapTileIndex) + "/" +
                        MapTileIndex.getX(pMapTileIndex) + "/" +
                        MapTileIndex.getY(pMapTileIndex) +
                        mImageFilenameEnding + "?api_key=" + apiKey
            }
        }

        mapView.setTileSource(mapaWatercolor)

        // 2. Nivel de zoom inicial y centro
        mapController.setZoom(14.0) // Lo alejamos un poco porque el máximo es 16
        val laPlataCenter = GeoPoint(-34.9205, -57.9536)
        mapController.setCenter(laPlataCenter)

        // 3. UX: Prevención de errores (Límites)
        val limitesLaPlata = BoundingBox(-34.88, -57.90, -34.97, -58.00)
        mapView.setScrollableAreaLimitDouble(limitesLaPlata)
        mapView.setMinZoomLevel(12.0)
        mapView.setMaxZoomLevel(16.0)
    }

    private fun aplicarFiltroCartografico() {
        // Generamos un tono pergamino usando el color "vinieta" / "texto2"
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(0f) // Quitamos los colores chillones de las calles (Sobrecarga cognitiva)

        // Tinte tierra/pergamino
        val sepiaMatrix = ColorMatrix(floatArrayOf(
            1.2f, 0.0f, 0.0f, 0.0f, 40.0f,
            0.0f, 1.0f, 0.0f, 0.0f, 20.0f,
            0.0f, 0.0f, 0.8f, 0.0f, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f, 0.0f
        ))
        colorMatrix.postConcat(sepiaMatrix)

        val filter = ColorMatrixColorFilter(colorMatrix)
        mapView.overlayManager.tilesOverlay.setColorFilter(filter)
    }

    private fun agregarMarcadorExcavacion(punto: GeoPoint) {
        val marcador = Marker(mapView)
        marcador.position = punto
        // Centramos el ancla para que el toque coincida con el centro visual
        marcador.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)

        // MEJORA A11Y: Lenguaje descriptivo. Evitamos instrucciones de hardware ("Toca una vez")
        // porque TalkBack maneja sus propias instrucciones ("Tocar dos veces para activar").
        marcador.title = "Punto de excavación"
        marcador.subDescription = "Fósil de Gliptodonte oculto"

        // El VectorDrawable debe medir 48dp x 48dp para cumplir WCAG (Target Size)
        marcador.icon = ContextCompat.getDrawable(mapView.context, R.drawable.ic_gliptodonte)

        marcador.setOnMarkerClickListener { _, _ ->
            // UX Heurística 3: Control y libertad / Visibilidad del estado
            // Redirigimos a la pantalla de excavación usando un Intent.
            val intent = Intent(this@MapaExcavacionActivity, ExcavacionActivity::class.java)

            // Opcional pero recomendado: Pasamos qué fósil es para que la ExcavacionActivity sepa qué cargar
            // intent.putExtra("TIPO_FOSIL", "gliptodonte")

            startActivity(intent)

            // Retornamos 'true' para consumir el evento y que el mapa no intente centrarse o mostrar la burbuja por defecto de osmdroid.
            true
        }

        mapView.overlays.add(marcador)
        mapView.invalidate()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }
}