package com.example.bt_dev.screens

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext


import com.example.bt_dev.services.BluetoothController
import com.example.bt_dev.services.BluetoothDevicesService
import com.example.bt_dev.widgets.DeviceList
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun BluetoothScreen(
    innerPadding: PaddingValues,
    activity: Activity,
    context: Context = LocalContext.current,
    bluetoothDevicesService: BluetoothDevicesService = koinInject(parameters = { parametersOf(context, activity)}),
    bluetoothController: BluetoothController = koinInject(parameters = { parametersOf(bluetoothDevicesService.btAdapter)})
) {
    DeviceList(
        innerPadding,
        activity,
        bluetoothDevicesService,
        bluetoothController
    )
}