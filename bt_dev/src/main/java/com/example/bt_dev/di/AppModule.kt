package com.example.bt_dev.di

import android.bluetooth.BluetoothAdapter
import android.content.Context
import com.example.bt_dev.services.BluetoothController
import com.example.bt_dev.services.BluetoothDevicesService
import org.koin.dsl.module

val appModule = module {
//    single { (context: Context) -> BluetoothDevicesService(context) }
//    single { (btAdapter: BluetoothAdapter) -> BluetoothController(btAdapter) }
    single { BluetoothDevicesService(get()) }
    single { BluetoothController(get()) }
}
