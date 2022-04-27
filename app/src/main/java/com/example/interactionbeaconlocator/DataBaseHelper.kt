package com.example.interactionbeaconlocator

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
//Se debe pasar un contexto como parametro.
class DataBaseHelper(context: Context):SQLiteOpenHelper(context,RegistroContract.Companion.Entrada.NOMBRE_TABLA,null,RegistroContract.Companion.VERSION) {

    companion object{
        val CREATE_TABLA_REGISTRO = "CREATE TABLE "+ RegistroContract.Companion.Entrada.NOMBRE_TABLA +
                " ("+RegistroContract.Companion.Entrada.COLUMNA_NOMBRE+" TEXT, "+
                RegistroContract.Companion.Entrada.COLUMNA_RSSI +" TEXT, "+
                RegistroContract.Companion.Entrada.COLUMNA_BCN +" TEXT, "+
                RegistroContract.Companion.Entrada.COLUMNA_INTEGRANTE + " TEXT)"

        val REMOVE_REGISTRO_TABLA = "DROP TABLE IF EXIST "+ RegistroContract.Companion.Entrada.NOMBRE_TABLA
    }
    //Se debe configurar la funcion onCreate y onUpgrade para construir la base de datos con las estructura adecuada.
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLA_REGISTRO)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL(REMOVE_REGISTRO_TABLA)
        onCreate(db)
    }
}