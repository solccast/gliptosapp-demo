package com.example.gliptosapp.ui.mapa

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.customview.widget.ExploreByTouchHelper
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
class MapaAccesibleHelper(
    private val hostView: View,
    private val mapView: MapView
) : ExploreByTouchHelper(hostView) {

    data class MarcadorAccesible(
        val id: Int,
        val position: GeoPoint,
        val descripcion: String,
        val onActivar: () -> Unit
    )

    private val marcadores = mutableListOf<MarcadorAccesible>()

    fun agregarMarcador(position: GeoPoint, descripcion: String, onActivar: () -> Unit) {
        marcadores.add(MarcadorAccesible(marcadores.size, position, descripcion, onActivar))
        invalidateRoot()
    }

    fun actualizarPosiciones() {
        invalidateRoot()
    }

    private fun pixelEnHostView(position: GeoPoint): android.graphics.Point {
        return mapView.projection.toPixels(position, null)
    }

    override fun getVirtualViewAt(x: Float, y: Float): Int {
        val radio = 80 * hostView.resources.displayMetrics.density
        return marcadores.firstOrNull { marcador ->
            val pixel = pixelEnHostView(marcador.position)
            Math.abs(pixel.x - x) < radio && Math.abs(pixel.y - y) < radio
        }?.id ?: INVALID_ID
    }

    override fun getVisibleVirtualViews(virtualViewIds: MutableList<Int>) {
        val bounds = mapView.boundingBox
        marcadores
            .filter { bounds.contains(it.position) }
            .forEach { virtualViewIds.add(it.id) }
    }

    override fun onPopulateNodeForVirtualView(
        virtualViewId: Int,
        node: AccessibilityNodeInfoCompat
    ) {
        val marcador = marcadores.getOrNull(virtualViewId) ?: return
        val pixel = pixelEnHostView(marcador.position)
        val size = (80 * hostView.resources.displayMetrics.density).toInt()

        node.contentDescription = marcador.descripcion
        node.addAction(AccessibilityNodeInfoCompat.ACTION_CLICK)
        node.className = "android.widget.Button"
        node.setBoundsInParent(
            Rect(
                pixel.x - size / 2,
                pixel.y - size / 2,
                pixel.x + size / 2,
                pixel.y + size / 2
            )
        )
    }

    override fun onPerformActionForVirtualView(
        virtualViewId: Int,
        action: Int,
        arguments: Bundle?
    ): Boolean {
        val marcador = marcadores.getOrNull(virtualViewId) ?: return false
        return when (action) {
            AccessibilityNodeInfoCompat.ACTION_CLICK -> {
                marcador.onActivar()
                true
            }
            else -> false
        }
    }
}