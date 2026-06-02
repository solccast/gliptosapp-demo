package com.example.gliptosapp.data

data class Fosil(
    val nombre: String,
    val descubierto: Boolean,
    val imgDescubierto: Int,
    val imgSinDescubrir: Int,
    val descripcion: String?
){
    fun obtenerImagen(): Int {
        return if (descubierto) imgDescubierto else imgSinDescubrir
    }
}
