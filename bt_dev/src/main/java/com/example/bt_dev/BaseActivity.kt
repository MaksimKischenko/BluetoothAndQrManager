package com.example.bt_dev

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext


import com.example.bluetoothmodule.ui.theme.BaseModuleTheme
import com.example.bt_dev.services.BluetoothController
import com.example.bt_dev.services.BluetoothDevicesService
import com.example.bt_dev.widgets.DeviceList

class BaseActivity : ComponentActivity() {
    private val activity = this
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val bluetoothDevicesService = BluetoothDevicesService.getInstanceAndInitAdapter(LocalContext.current, activity).instance
            val bluetoothDevicesAdapter = BluetoothDevicesService.getInstanceAndInitAdapter(LocalContext.current, activity).adapter
            val bluetoothController = BluetoothController(bluetoothDevicesAdapter!!)
            bluetoothDevicesService.checkPermissions(this, activity)
            bluetoothDevicesService.registerIntentFilters(activity)
            BaseModuleTheme {
                BaseContent(
                    activity,
                    bluetoothDevicesService,
                    bluetoothController
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun BaseContent(
    activity: Activity,
    bluetoothDevicesService: BluetoothDevicesService,
    bluetoothController: BluetoothController
) {
    DeviceList(
        activity,
        bluetoothDevicesService,
        bluetoothController
    )
}

