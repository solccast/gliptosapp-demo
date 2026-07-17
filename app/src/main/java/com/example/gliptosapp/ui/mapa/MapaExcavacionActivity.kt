package com.example.gliptosapp.ui.mapa

import android.content.Intent
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.preference.PreferenceManager
import android.view.View
import android.view.accessibility.AccessibilityManager
import android.widget.ImageButton
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.customview.widget.ExploreByTouchHelper
import androidx.lifecycle.lifecycleScope
import com.example.gliptosapp.R
import com.example.gliptosapp.data.entities.Excavacion
import com.example.gliptosapp.ui.excavation.ExcavacionActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.util.MapTileIndex
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker

@AndroidEntryPoint // <-- Hilt inyecta todo lo necesario aquí
class MapaExcavacionActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private val marcadores = mutableListOf<Marker>()
    private lateinit var accessibilityHelper: MapaAccesibleHelper
    private val viewModel: MapaViewModel by viewModels()

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

        viewAccesibilidad.isClickable = false
        viewAccesibilidad.isFocusable = false

        viewAccesibilidad.setOnHoverListener { _, event ->
            accessibilityHelper.dispatchHoverEvent(event)
        }

        ViewCompat.replaceAccessibilityAction(
            viewAccesibilidad,
            AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLICK,
            "Explorar zona"
        ) { _, _ ->
            if (isTalkBackActivo()) {
                feedbackZonaVacia()
            }
            true
        }

        configurarMapa()
        configurarTapEnZonaVacia()

        // OBSERVAMOS LA BASE DE DATOS REACTIVAMENTE
        lifecycleScope.launch {
            viewModel.excavaciones.collect { listaFosiles ->
                pintarMarcadores(listaFosiles)
            }
        }

        mapView.post {
            mostrarInstruccionesIniciales()
        }

        btnVolver.setOnClickListener { finish() }

        btnZoomIn.setOnClickListener {
            mapView.controller.zoomIn()
            mapView.announceForAccessibility("Zoom nivel ${mapView.zoomLevelDouble.toInt()}")
        }

        btnZoomOut.setOnClickListener {
            mapView.controller.zoomOut()
            mapView.announceForAccessibility("Zoom nivel ${mapView.zoomLevelDouble.toInt()}")
        }

        btnAyuda.setOnClickListener {
            mostrarInstruccionesIniciales()
        }
    }

    private fun pintarMarcadores(listaFosiles: List<Excavacion>) {
        // Limpiamos overlays anteriores (marcadores y pistas)
        mapView.overlays.clear()
        marcadores.clear()

        // Es vital volver a agregar el overlay de eventos de tap vacío al principio
        configurarTapEnZonaVacia()

        listaFosiles.forEach { fosil ->
            agregarMarcadorExcavacion(fosil)
        }

        agregarZonasDePista()
        mapView.invalidate()
    }

    private fun escalarDrawable(nombreIcono: String, anchoDp: Int): android.graphics.drawable.BitmapDrawable {
        val anchoPx = (anchoDp * resources.displayMetrics.density).toInt()
        val drawable = ContextCompat.getDrawable(mapView.context, obtenerIdDrawable(nombreIcono))
            ?: return android.graphics.drawable.BitmapDrawable(resources, null as android.graphics.Bitmap?)

        val anchoOriginal = drawable.intrinsicWidth.takeIf { it > 0 } ?: anchoPx
        val altoOriginal = drawable.intrinsicHeight.takeIf { it > 0 } ?: anchoPx
        val altoPx = (altoOriginal * anchoPx.toFloat() / anchoOriginal).toInt()

        val bitmap = android.graphics.Bitmap.createBitmap(anchoPx, altoPx, android.graphics.Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bitmap)
        drawable.setBounds(0, 0, anchoPx, altoPx)
        drawable.draw(canvas)
        return android.graphics.drawable.BitmapDrawable(resources, bitmap)
    }
    private fun agregarMarcadorExcavacion(fosil: Excavacion) {
        val punto = GeoPoint(fosil.latitud, fosil.longitud)

        val marcador = Marker(mapView)
        marcador.position = punto
        marcador.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)

        marcador.icon = escalarDrawable(fosil.icResName, 54)

        marcador.setOnMarkerClickListener { _, _ ->
            if (!isTalkBackActivo()) navegarAExcavacion(fosil.id)
            true
        }

        marcadores.add(marcador)
        mapView.overlays.add(marcador)

        accessibilityHelper.agregarMarcador(
            position = punto,
            descripcion = "Zona de excavación. Posible fósil de ${fosil.nombre} oculto.",
            onActivar = { navegarAExcavacion(fosil.id) }
        )
    }

    private fun obtenerIdDrawable(nombreIcono: String): Int {
        // Elimina la extensión si viene con .webp, .png, etc.
        val nombreLimpio = nombreIcono.substringBeforeLast(".")

        return resources.getIdentifier(nombreLimpio, "drawable", packageName)
            .takeIf { it != 0 } ?: R.drawable.ic_gliptodonte
    }

    private fun isTalkBackActivo(): Boolean {
        val am = getSystemService(ACCESSIBILITY_SERVICE) as AccessibilityManager
        return am.isEnabled && am.isTouchExplorationEnabled
    }

    private fun mostrarInstruccionesIniciales() {
        if (isTalkBackActivo()) {
            mapView.announceForAccessibility(
                "Mapa de La Plata. Explorá la pantalla para encontrar " +
                        "zonas de excavación. " +
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
                VibrationEffect.createOneShot(150, 230)
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(150)
        }

        mapView.announceForAccessibility(
            "En esta zona no hicieron denuncias de fósiles. " +
                    "Intentá explorar hacia el centro de la ciudad."
        )
    }

    private fun configurarTapEnZonaVacia() {
        val receptor = object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                if (isTalkBackActivo()) return false

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

    private fun radioEnPixeles(): Float {
        return when {
            mapView.zoomLevelDouble >= 15 -> 60f
            mapView.zoomLevelDouble >= 13 -> 80f
            else -> 100f
        }
    }

    private fun configurarMapa() {
        mapView.setMultiTouchControls(true)
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

    private fun agregarZonasDePista() {
        val overlayPistas = object : org.osmdroid.views.overlay.Overlay() {
            override fun draw(
                canvas: android.graphics.Canvas,
                mapView: MapView,
                shadow: Boolean
            ) {
                if (shadow) return
                val paintFondo = android.graphics.Paint().apply {
                    color = android.graphics.Color.parseColor("#33D85A3C")
                    style = android.graphics.Paint.Style.FILL
                }
                val paintBorde = android.graphics.Paint().apply {
                    color = android.graphics.Color.parseColor("#D85A3C")
                    style = android.graphics.Paint.Style.STROKE
                    strokeWidth = 5f
                    pathEffect = DashPathEffect(floatArrayOf(15f, 10f), 0f)
                }

                marcadores.forEach { marcador ->
                    val pixel = mapView.projection.toPixels(marcador.position, null)
                    val radio = 54 * resources.displayMetrics.density
                    canvas.drawCircle(pixel.x.toFloat(), pixel.y.toFloat(), radio, paintFondo)
                    canvas.drawCircle(pixel.x.toFloat(), pixel.y.toFloat(), radio, paintBorde)
                }
            }
        }
        mapView.overlays.add(overlayPistas)
    }

    // Pasamos el ID del fósil para que la siguiente pantalla sepa cuál cargar
    private fun navegarAExcavacion(idFosil: Int) {
        val intent = Intent(this, ExcavacionActivity::class.java).apply {
            putExtra("FOSIL_ID", idFosil)
        }
        startActivity(intent)
    }

    override fun onResume() { super.onResume(); mapView.onResume() }
    override fun onPause() { super.onPause(); mapView.onPause() }
}