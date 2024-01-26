package com.example.bt_dev

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable


import com.example.bluetoothmodule.ui.theme.BaseModuleTheme
import com.example.bt_dev.services.BluetoothService
import com.example.bt_dev.widgets.DeviceList

class BaseActivity : ComponentActivity() {
    private val activity = this
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BaseModuleTheme {
                BaseContent(activity)
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun BaseContent(activity: Activity) {
    DeviceList(activity)
}

