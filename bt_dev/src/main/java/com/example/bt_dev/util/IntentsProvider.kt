package com.example.bt_dev.util

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.IntentFilter
import android.provider.Settings

class IntentsProvider {
    companion object {
        val actionRequestEnableBluetoothAdapter = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE) //для включения BT
        val actionRequestEnableLocationSettings = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS) //для включения Location
        val actionFoundBluetoothDeviceFilter = IntentFilter(BluetoothDevice.ACTION_FOUND) //ждем найденные устройства
        val actionBondStateChangedBluetoothDeviceFilter = IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED) //когда изменится состояние сопряжения
        val actionDiscoveryFinishedBluetoothAdapterFilter = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
    }
}