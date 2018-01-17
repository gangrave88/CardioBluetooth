package com.gangrave88.cardiobluetooth

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.bt_item_device.view.*

class DeviceListAdapter(val devices:MutableSet<BTDevice>): RecyclerView.Adapter<DeviceListAdapter.Holder>() {
    override fun onBindViewHolder(holder: Holder?, position: Int) {
        holder?.bind(devices.elementAt(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.bt_item_device, parent, false)
        return Holder(v)
    }

    override fun getItemCount(): Int {
        return devices.size
    }

    class Holder(itemView:View):RecyclerView.ViewHolder(itemView){

        fun bind(device:BTDevice){
            itemView.tv_bt_name.text = device.name
            itemView.tv_bt_clazz.text = device.clazz
            itemView.tv_bt_mac.text = device.MAC
        }

    }
}