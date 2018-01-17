package com.gangrave88.cardiobluetooth

import com.github.pwittchen.reactivebeacons.library.rx2.Beacon

data class BTDevice constructor(var name:String="", var clazz:String="", var MAC:String="", var beckon:Beacon)