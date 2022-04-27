package com.example.interactionbeaconlocator

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

class RegistroCRUD(context:Context) {

    private var helper:DataBaseHelper?=null//Helper que contiene la estructura y instruccines para crear la base de datos.
    init{
        helper= DataBaseHelper(context)
    }

    //Crear las funciones CRUD
    //Crear nuevo registro.

    fun newRegistro(item:Registro){
        val db:SQLiteDatabase=helper?.writableDatabase!!//Definir las operaciones que se van a realizar antes de abrir la base de datos.
        val values = ContentValues()//Clase que permite agrupar información que será ingresada a la tabla.
        //Mapeo de columnas con valores  a insertar.
        values.put(RegistroContract.Companion.Entrada.COLUMNA_NOMBRE,item.Nombre)
        values.put(RegistroContract.Companion.Entrada.COLUMNA_RSSI,item.Rssi)
        values.put(RegistroContract.Companion.Entrada.COLUMNA_BCN,item.Bcn)
        values.put(RegistroContract.Companion.Entrada.COLUMNA_INTEGRANTE,item.Integrante)

        //Insertar una nueva fila en la tabla.
        val newRowNombre=db.insert(RegistroContract.Companion.Entrada.NOMBRE_TABLA,null,values)//Ingreso de registro.
        db.close()//Cerrar la conexion.

    }

    fun getRegistros():ArrayList<Registro>{
        val items:ArrayList<Registro> = ArrayList()
        //Arbrir la base de datos en modo consulta.
        val db:SQLiteDatabase=helper?.readableDatabase!!
        //Especificar las columnas ques es quieren consultar.
        val columnas=arrayOf(RegistroContract.Companion.Entrada.COLUMNA_NOMBRE,RegistroContract.Companion.Entrada.COLUMNA_RSSI,RegistroContract.Companion.Entrada.COLUMNA_BCN,RegistroContract.Companion.Entrada.COLUMNA_INTEGRANTE)

        //Crear uin cursor para recorrer la tbla.
        val c: Cursor = db.query(
            RegistroContract.Companion.Entrada.NOMBRE_TABLA,
            columnas,
            null,
            null,
            null,
            null,
            null
        )
        //Realizar el recorrido del cursor pór la tabla.
        while (c.moveToNext()){
            items.add(
                Registro(
                c.getString(c.getColumnIndexOrThrow(RegistroContract.Companion.Entrada.COLUMNA_NOMBRE)),
                    c.getString(c.getColumnIndexOrThrow(RegistroContract.Companion.Entrada.COLUMNA_RSSI)),
                    c.getString(c.getColumnIndexOrThrow(RegistroContract.Companion.Entrada.COLUMNA_BCN)),
                    c.getString(c.getColumnIndexOrThrow(RegistroContract.Companion.Entrada.COLUMNA_INTEGRANTE))
            ))
        }
        //Cerrar la base de datos.
        db.close()
        return items
    }
}