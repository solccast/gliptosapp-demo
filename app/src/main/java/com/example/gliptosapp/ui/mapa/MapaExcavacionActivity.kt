package com.example.gliptosapp.ui.mapa

import org.osmdroid.tileprovider.tilesource.XYTileSource
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
import com.example.gliptosapp.R

class MapaExcavacionActivity : AppCompatActivity() {

    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. IMPORTANTE: Inicializar la configuración de osmdroid ANTES del layout
        Configuration.getInstance().load(
            applicationContext,
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )

        setContentView(R.layout.activity_mapa_excavacion)

        mapView = findViewById(R.id.mapView)
        val btnVolver = findViewById<Button>(R.id.btnVolver)

        configurarMapa()
        //aplicarFiltroCartografico()
        agregarMarcadorExcavacion(GeoPoint(-34.9205, -57.9536)) // Plaza Moreno (Ejemplo)

        btnVolver.setOnClickListener {
            finish() // Cierra la actividad y vuelve al menú
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
        marcador.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

        // ACCESIBILIDAD (WCAG): Descripción para lectores de pantalla
        marcador.title = "Zona de excavación disponible"
        marcador.subDescription = "Toca dos veces para usar tus herramientas aquí."

        // TODO: Aquí debes cargar tu ícono personalizado.
        // Asegúrate de que el ícono (drawable) tenga al menos 48x48dp
        // y use el color "errores" (#D85A3C) para garantizar alto contraste sobre el mapa filtrado.
        // marcador.icon = ContextCompat.getDrawable(this, R.drawable.ic_pin_excavacion_rojo)

        marcador.setOnMarkerClickListener { marker, _ ->
            // Iniciar la lógica/Activity de las herramientas de excavación
            // Pasar el ID del fósil por intent
            true
        }

        mapView.overlays.add(marcador)
        mapView.invalidate() // Refrescar el mapa
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