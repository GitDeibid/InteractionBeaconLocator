package com.example.interactionbeaconlocator

class Registro(nombre:String,rssi:String,bcn:String,integrante:String) {
    var Nombre:String?=null
    var Rssi:String?=null
    var Bcn:String?=null
    var Integrante:String?=null

    init {
        this.Nombre=nombre
        this.Rssi=rssi
        this.Bcn=bcn
        this.Integrante=integrante
    }
}