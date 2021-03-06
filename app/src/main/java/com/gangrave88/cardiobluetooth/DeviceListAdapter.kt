package com.gangrave88.cardiobluetooth

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.bt_item_device.view.*

class DeviceListAdapter(var devices:MutableSet<BTDevice>?): RecyclerView.Adapter<DeviceListAdapter.Holder>() {

    init {
        if (devices==null)
            devices = mutableSetOf()
    }

    override fun onBindViewHolder(holder: Holder?, position: Int) {
        if (devices != null) devices?.elementAt(position)?.let { holder?.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.bt_item_device, parent, false)
        return Holder(v)
    }

    override fun getItemCount(): Int {
        if (devices != null) {
            return devices!!.size
        }
        return 0
    }

    class Holder(itemView:View):RecyclerView.ViewHolder(itemView){

        fun bind(device:BTDevice){
            itemView.tv_bt_name.text = device.name
            itemView.tv_bt_clazz.text = device.clazz
            itemView.tv_bt_mac.text = device.MAC
        }

    }
}