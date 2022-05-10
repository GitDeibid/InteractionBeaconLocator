package com.example.interactionbeaconlocator

import android.Manifest
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.interactionbeaconlocator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    var crud:RegistroCRUD?=null
    //Variables relacionadas al uso del escaner bluetooth.
    private val adaptador:BluetoothAdapter by lazy {
        (getSystemService(BLUETOOTH_SERVICE) as BluetoothManager).adapter
    }

    private var bluetoothLeScanner:BluetoothLeScanner?= null
    //private val escaner = adaptador.bluetoothLeScanner
    private var scanning = false
    private val handler = Handler()
    private val SCAN_PERIOD:Long = 10000
    var ROL:String = "Anónimo"
    private val leScanCallback = object : ScanCallback() {

        @SuppressLint("MissingPermission")
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)

            var rssi=result?.rssi.toString()
            var adress = result?.device?.address.toString()
            var nombre = result?.device?.name.toString()
            if (adress=="60:77:71:8E:69:B9" || adress == "60:77:71:8E:72:85"){
                crud?.newRegistro(Registro(nombre,rssi,adress,ROL))
                Log.d("ESCANER","onScanResult: ${result?.device?.name}")
            }
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            super.onBatchScanResults(results)
            Log.d("ESCANER","onBatchScanResults:${results.toString()}")
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            Log.d("ESCANER", "onScanFailed: $errorCode")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Solicitar los permisos necesarios para escaner bluetooth y exportar el archivo con los datos.
        revisarPermisos()
        //Boton Escanear.
        //Configuración BLE
        bluetoothLeScanner = adaptador.bluetoothLeScanner
        crud = RegistroCRUD(this)

        //Drop down menú de roles scrum de la  vista principal.
        val roles = resources.getStringArray(R.array.scrum_rols)
        val rol_adapter = ArrayAdapter(this,R.layout.rols_items,roles)
        binding.DDRoles.setAdapter(rol_adapter)


        binding.btnEscanear.setOnClickListener{
            //Barra de progreso de avance de escanner.
            binding.progressBar.max=10000
            //val currentProgress = 6000
            ObjectAnimator.ofInt(binding.progressBar,"progress",10000).setDuration(SCAN_PERIOD).start()

            if (adaptador.isEnabled){
                ROL = binding.DDRoles.text.toString()
                //Se debe escanear en bsuca de los dispositivos.
                escanearBLE()
            }
            //crud?.newRegistro(Registro(Nombre_texto!!,Rssi_texto!!,BCN_texto!!,INT_texto!!))
            //startActivity(Intent(this,Listado::class.java))
        }

        binding.btnMostrar.setOnClickListener{
            startActivity(Intent(this,Listado::class.java))
        }

        binding.btnLimpiar.setOnClickListener{
            crud?.clearList()
        }
    }

    private fun revisarPermisos() {
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this,Manifest.permission.BLUETOOTH)!=PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this,Manifest.permission.BLUETOOTH_SCAN)!= PackageManager.PERMISSION_GRANTED
                ){
            //El permiso no ha sido aceptado por eel momento.
            solicitarPermiso()
        }
    }

    private fun solicitarPermiso() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.BLUETOOTH,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN
            ),777)
    }

    private fun escanearBLE(){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.checkSelfPermission(this,Manifest.permission.BLUETOOTH_SCAN)
            Log.d("ESCANER","Permiso de scanner no entregado...")
        }
        if (!scanning) { // Stops scanning after a pre-defined scan period.
            handler.postDelayed({
                scanning = false
                bluetoothLeScanner!!.stopScan(leScanCallback)
                Log.d("ESCANER","Deteniendo el escaneo")
            }, SCAN_PERIOD)
            scanning = true
            bluetoothLeScanner!!.startScan(leScanCallback)
            Log.d("ESCANER","Se inicia el escaneo")
        } else {
            scanning = false
            bluetoothLeScanner!!.stopScan(leScanCallback)
            Log.d("ESCANER","Deteniendo el escaneo")
        }
    }

}

