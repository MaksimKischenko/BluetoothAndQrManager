package com.example.bt_dev.bottom_nav

import com.example.bt_dev.R

sealed class BottomItem(val title: String, val iconId:Int, val route: String) {
    data object BluetoothScreenItem: BottomItem("Bluetooth", R.drawable.bluetooth_searching, "bluetoothScreen")
    data object QRCamScreenItem: BottomItem("QRCam", R.drawable.qr_code_scanner, "qRCamScreen")

}
