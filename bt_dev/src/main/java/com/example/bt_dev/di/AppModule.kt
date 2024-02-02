package com.example.bt_dev.di

import android.bluetooth.BluetoothAdapter
import android.content.Context
import com.example.bt_dev.services.BluetoothController
import com.example.bt_dev.services.BluetoothDevicesService
import com.example.bt_dev.services.QrScanService
import com.example.bt_dev.viewmodel.SearchDevicesViewModel
import org.koin.androidx.compose.get
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
// implementation ("io.insert-koin:koin-androidx-compose:3.5.3")
val appModule = module {
    single { BluetoothDevicesService(get(), get()) }
    single { QrScanService(get()) }
    viewModel { SearchDevicesViewModel(get()) }
    single { BluetoothController(get()) }
}
