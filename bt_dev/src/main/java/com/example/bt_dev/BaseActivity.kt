package com.example.bt_dev

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext


import com.example.bluetoothmodule.ui.theme.BaseModuleTheme
import com.example.bt_dev.services.BluetoothService
import com.example.bt_dev.widgets.DeviceList

class BaseActivity : ComponentActivity() {
    private val activity = this
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val bluetoothService = BluetoothService.getInstanceAndInitAdapter(LocalContext.current, activity).instance
            bluetoothService.checkPermissions(this, activity)
            bluetoothService.registerIntentFilters(activity)
            BaseModuleTheme {
                BaseContent(activity, bluetoothService)
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun BaseContent(activity: Activity, bluetoothService: BluetoothService) {
    DeviceList(activity, bluetoothService)
}

