package com.gangrave88.cardiobluetooth

import android.bluetooth.BluetoothDevice
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import me.aflak.bluetooth.Bluetooth
import me.aflak.bluetooth.CommunicationCallback

class MainActivity : AppCompatActivity() {

    private lateinit var adapter:DeviceListAdapter
    lateinit var bluetooth:Bluetooth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = DeviceListAdapter(null)
        bluetooth = Bluetooth(this)

        bluetooth.onStart()

        current_device.text = "Текущее устройство: "

        val rv = rv_device
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        if (!bluetooth.isEnabled)
            bluetooth.enable()

        val pairD = bluetooth.pairedDevices
        var addres=""
        pairD.map {
            if (it.name=="Bluedio")
                addres = it.address
            addDevice(BTDevice(it.name,it.bluetoothClass.toString() ,it.address))
        }

        bluetooth.setCommunicationCallback(object : CommunicationCallback{
            override fun onConnect(device: BluetoothDevice?) {
                Toast.makeText(this@MainActivity, "Есть контакт", Toast.LENGTH_LONG).show()
            }

            override fun onConnectError(device: BluetoothDevice?, message: String?) {
                Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
            }

            override fun onMessage(message: String?) {
                Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
            }

            override fun onDisconnect(device: BluetoothDevice?, message: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onError(message: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })

        bluetooth.connectToAddress(addres)
    }

    override fun onStop() {
        super.onStop()
        bluetooth.onStop()
    }

    private fun addDevice(device:BTDevice){
        adapter.devices?.add(device)
        adapter.notifyDataSetChanged()
    }

}
