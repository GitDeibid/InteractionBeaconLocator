package com.example.interactionbeaconlocator

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdaptadorCustom(items:ArrayList<Registro>):RecyclerView.Adapter<AdaptadorCustom.ViewHolder>() {
    var items:ArrayList<Registro>?=null
    //var viewHolder:ViewHolder?=null
    init {
        this.items=items
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdaptadorCustom.ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.template_registro,parent,false)
        val viewHolder=ViewHolder(vista)//Reenderiza la vista.
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items?.get(position)
        holder.info?.text="Nombre: "+item?.Nombre+"\n"+
                "Rssi: "+item?.Rssi+
                "\n"+"Beacon: "+item?.Bcn+
                "\n"+"ROL: "+item?.Rol
    }

    override fun getItemCount(): Int {
        return items?.count()!!
    }

    class ViewHolder(vista:View):RecyclerView.ViewHolder(vista){
        var vista=vista
        var info:TextView?=null

        init{
            info = vista.findViewById(R.id.filaRegistro)
        }
    }
}