package com.example.interactionbeaconlocator

import android.provider.BaseColumns

class RegistroContract {
    companion object{
        //Base COlum peermite mapear las columnas dentro de la base de datos.
        val VERSION=1
        class Entrada: BaseColumns{
            companion object{
                val NOMBRE_TABLA = "Registros"
                val COLUMNA_NOMBRE = "nombre"
                val COLUMNA_RSSI = "rssi"
                val COLUMNA_BCN = "bcn"
                val COLUMNA_INTEGRANTE = "rol"
            }
        }
    }
}