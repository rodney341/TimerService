package com.example.timerservice

data class Seguimiento(var longitud: Double, var latitud: Double) {
    companion object {
        val SIN_POSICION = Seguimiento(0.0,0.0)
    }
}