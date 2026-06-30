package com.example.gliptosapp.data

data class ComparativeGame (
    val textoPregunta: String,
    val opciones: List<OptionComparativeGame>,
    val realizada: Boolean,
    val infoExtra: String // texto que se muestra al acertar
)