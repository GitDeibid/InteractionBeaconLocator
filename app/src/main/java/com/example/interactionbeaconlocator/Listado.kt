package com.example.interactionbeaconlocator

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.interactionbeaconlocator.databinding.ActivityListadoBinding
import java.io.File
import java.io.FileWriter
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


class Listado : AppCompatActivity() {

    var lista:RecyclerView?=null
    var adaptador:AdaptadorCustom?=null
    var layoutManager:RecyclerView.LayoutManager?=null//El layout manager permite administrar el layout.
    var registros:ArrayList<Registro>?=null
    var crud:RegistroCRUD?=null
    private lateinit var binding_list: ActivityListadoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_listado)

        binding_list = ActivityListadoBinding.inflate(layoutInflater)
        setContentView(binding_list.root)
        //Configuración de la lista que contendrá los datos leidos desde la base de datos.
        lista = binding_list.lista
        //lista?.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        lista?.layoutManager=layoutManager

        crud = RegistroCRUD(this)
        //Arreglo de datos para mostrar con  adaptador.
        registros = crud?.getRegistros()!!//Se rellena la lista con los datos desde la base de datos.

        adaptador  = AdaptadorCustom(registros!!)
        lista?.adapter=adaptador

        //Botones:
        binding_list.btnVolverMP.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }
        binding_list.btnExportar.setOnClickListener {

            ExportarCSV(registros!!)
        }

    }

    private fun ExportarCSV(listado:ArrayList<Registro>) {
        checkExternalStoragePermission()
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        var currentDate:String = sdf.format(Date())

        val dataFile = File("/storage/emulated/0/Download/Data.csv")
        val email="darm1452@gmail.com"
        val subject="Datos generados el "+currentDate
        val msg = "Información enviada desde aplicación IBL"

        var archivo = FileWriter("/storage/emulated/0/Download/Data.csv")
        for (i in listado){
            archivo.write(i.Nombre+","+i.Rssi+","+i.Bcn+","+i.Rol+"\n")
        }
        archivo.close()
        val fileURI = FileProvider.getUriForFile(this,BuildConfig.APPLICATION_ID + ".fileprovider",dataFile)
        val intento_enviar = Intent(Intent.ACTION_SEND).apply {//No puede ser SEDNTO, no abre la aplicacion para enviar.
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL,email)
            putExtra(Intent.EXTRA_SUBJECT,subject)
            putExtra(Intent.EXTRA_STREAM,fileURI)
            setType("text/html")//Necesario para poder seleccionar una aplicación

        }
        try{
            startActivity(intento_enviar)
        }catch (e:Exception){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkExternalStoragePermission() {
        val permissionCheck = ContextCompat.checkSelfPermission(
            this, Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.i("Mensaje", "No se tiene permiso para leer.")
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                225
            )
        } else {
            Log.i("Mensaje", "Se tiene permiso para leer!")
        }
    }
}
