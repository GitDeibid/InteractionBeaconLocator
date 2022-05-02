package com.example.interactionbeaconlocator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.interactionbeaconlocator.databinding.ActivityListadoBinding
import com.example.interactionbeaconlocator.databinding.ActivityMainBinding

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
    }
}
