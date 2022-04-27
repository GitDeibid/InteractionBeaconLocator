package com.example.interactionbeaconlocator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.interactionbeaconlocator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    var crud:RegistroCRUD?=null
    var Nombre_texto:String?=null
    var Rssi_texto:String?=null
    var BCN_texto:String?=null
    var INT_texto:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Boton Escanear.

        crud = RegistroCRUD(this)

        binding.btnEscanear.setOnClickListener{
            Nombre_texto = binding.etNombre.text.toString()
            Rssi_texto = binding.etRssi.text.toString()
            BCN_texto = binding.etBeacon.text.toString()
            INT_texto = binding.etIntegrante.toString()

            crud?.newRegistro(Registro(Nombre_texto!!,Rssi_texto!!,BCN_texto!!,INT_texto!!))

        }
    }


}