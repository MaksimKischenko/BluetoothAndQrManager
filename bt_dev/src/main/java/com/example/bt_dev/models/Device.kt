package com.example.bt_dev.models

import android.bluetooth.BluetoothDevice

data class Device(
    var device: BluetoothDevice?,
    var isSelected: Boolean
)
