package com.gangrave88.cardiobluetooth

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.github.pwittchen.reactivebeacons.library.rx2.ReactiveBeacons
import kotlinx.android.synthetic.main.activity_main.*
import com.github.pwittchen.reactivebeacons.library.rx2.Beacon
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import android.annotation.SuppressLint
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import io.reactivex.disposables.Disposable

class MainActivity : AppCompatActivity() {

    lateinit var devices:MutableSet<BTDevice>
    lateinit var adapter:DeviceListAdapter
    lateinit var rxBeacon:ReactiveBeacons
    private var subscription:Disposable? = null
    private val IS_AT_LEAST_ANDROID_M = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        devices = mutableSetOf()

        adapter = DeviceListAdapter(devices)

        rxBeacon = ReactiveBeacons(this)

        current_device.text = "Текущее устройство: "

        val rv = rv_device
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
    }

    fun addDevice(beacon: Beacon){
            devices.add(BTDevice("","","",beacon))
    }

    fun refreshDevice(){

        val list = devices.map {
            it.name = it.beckon.device.name
            it.clazz = it.beckon.proximity.toString()
            it.MAC = it.beckon.device.address
        }

        adapter.devices.clear()
        adapter.devices.zip(list)
        adapter.notifyDataSetChanged()
    }

    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()

        if (!rxBeacon.isBleSupported){
            Toast.makeText(this, "Все плохо!!!",Toast.LENGTH_LONG).show()
        }

        if (!canObserveBeacons()) {
            return
        }

        subscription = (rxBeacon).observe()
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { beacon -> addDevice(beacon); refreshDevice()}
    }

    override fun onPause() {
        super.onPause()

        if (subscription != null && !subscription!!.isDisposed()) {
            subscription!!.dispose();
        }
    }

    private fun canObserveBeacons(): Boolean {

        if (rxBeacon != null) {

            if (!(rxBeacon as ReactiveBeacons).isBleSupported) {
                Toast.makeText(this, "BLE is not supported on this device", Toast.LENGTH_SHORT).show()
                return false
            }

            if (!(rxBeacon as ReactiveBeacons).isBluetoothEnabled) {
                (rxBeacon as ReactiveBeacons).requestBluetoothAccess(this)
                return false
            } else if (!(rxBeacon as ReactiveBeacons).isLocationEnabled(this)) {
                (rxBeacon as ReactiveBeacons).requestLocationAccess(this)
                return false
            } else if (!isFineOrCoarseLocationPermissionGranted() && IS_AT_LEAST_ANDROID_M) {
                requestCoarseLocationPermission()
                return false
            }

            return true
        }

        return false
    }

    private fun requestCoarseLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf<String>(ACCESS_COARSE_LOCATION),
                    1000)
        }
    }

    private fun isFineOrCoarseLocationPermissionGranted(): Boolean {
        val isAndroidMOrHigher = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
        val isFineLocationPermissionGranted = isGranted(ACCESS_FINE_LOCATION)
        val isCoarseLocationPermissionGranted = isGranted(ACCESS_COARSE_LOCATION)

        return isAndroidMOrHigher && (isFineLocationPermissionGranted || isCoarseLocationPermissionGranted)
    }

    private fun isGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(this, permission) == PERMISSION_GRANTED
    }
}
