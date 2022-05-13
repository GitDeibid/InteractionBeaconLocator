package com.example.interactionbeaconlocator

import android.Manifest
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
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
    private val scanConfig = ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_POWER).build()
    //private val escaner = adaptador.bluetoothLeScanner
    private var scanning = false
    private val handler = Handler()
    var SCAN_PERIOD:Long = 60000
    var time_minutos:Int?=null
    var ROL:String? = "Anónimo"
    var rssi:String?=null
    var adress:String?=null
    var nombre:String?=null

    private val leScanCallback = object : ScanCallback() {

        @SuppressLint("MissingPermission")
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)

            rssi=result?.rssi.toString()
            adress = result?.device?.address.toString()
            nombre = result?.device?.name.toString()
            //if (adress=="60:77:71:8E:69:B9" || adress == "60:77:71:8E:72:85")
            if (result?.device!=null){
                crud?.newRegistro(Registro(nombre!!,rssi!!,adress!!,ROL!!))
                Log.d("ESCANER","Dispositivo: ${result?.device?.name}")
            }

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

        //Drop down menú de periodos de tiempo de escaner.
        val escaner_time = resources.getStringArray(R.array.scanner_times)
        val escaner_time_adapter = ArrayAdapter(this,R.layout.time_list,escaner_time)
        binding.DDTime.setAdapter(escaner_time_adapter)
        //Barra de progreso de avance de escanner.
        var barraCarga = binding.progressBar
        barraCarga.max=9000
        //Tiempo de escanner.
        var tiempo = binding.DDTime.text.toString()
        //Boton de escanear.
        binding.btnEscanear.setOnClickListener{
            if (adaptador.isEnabled){
                if(tiempo!=""){
                    SCAN_PERIOD = tiempo.toLong()*60*1000
                }
                //val currentProgress = 6000
                ObjectAnimator.ofInt(barraCarga,"progress",9000).setDuration(SCAN_PERIOD).start()
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
            ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED ||
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
            Manifest.permission.BLUETOOTH_SCAN,Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE
            ),777)
    }

    @SuppressLint("MissingPermission")
    private fun escanearBLE(){
        /*if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.checkSelfPermission(this,Manifest.permission.BLUETOOTH_SCAN)
            Log.d("ESCANER","Permiso de scanner no entregado...")
        }*/
        if (!scanning) { // Stops scanning after a pre-defined scan period.
            handler.postDelayed({
                scanning = false
                bluetoothLeScanner!!.stopScan(leScanCallback)
                Log.d("ESCANER","Deteniendo el escaneo")
            }, SCAN_PERIOD)
            scanning = true
            bluetoothLeScanner!!.startScan(null,scanConfig,leScanCallback)
            Log.d("ESCANER","Se inicia el escaneo")
        } else {
            scanning = false
            bluetoothLeScanner!!.stopScan(leScanCallback)
            Log.d("ESCANER","Deteniendo el escaneo")
        }
    }

}

