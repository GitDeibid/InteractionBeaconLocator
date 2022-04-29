package com.example.interactionbeaconlocator

class Registro(nombre:String,rssi:String,bcn:String,rol:String) {
    var Nombre:String?=null
    var Rssi:String?=null
    var Bcn:String?=null
    var Rol:String?=null

    init {
        this.Nombre=nombre
        this.Rssi=rssi
        this.Bcn=bcn
        this.Rol=rol
    }
}