package com.example.bt_dev

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext


import com.example.bluetoothmodule.ui.theme.BaseModuleTheme
import com.example.bt_dev.di.appModule
import com.example.bt_dev.services.BluetoothController
import com.example.bt_dev.services.BluetoothDevicesService
import com.example.bt_dev.widgets.DeviceList
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.compose.koinInject
import org.koin.core.context.startKoin
import org.koin.core.parameter.parametersOf

class BaseActivity : ComponentActivity() {
    private val activity = this
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startKoin {
            androidLogger()
            modules(appModule)
        }
        setContent {
            BaseModuleTheme {
                BaseContent(
                    activity,
                    LocalContext.current
                )
            }
        }
    }
}



@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun BaseContent(
    activity: Activity,
    context: Context,
    bluetoothDevicesService: BluetoothDevicesService = koinInject(parameters = { parametersOf(context)}),
    bluetoothController: BluetoothController = koinInject(parameters = { parametersOf(bluetoothDevicesService.btAdapter)})
) {
    bluetoothDevicesService.checkPermissions(context, activity)
    bluetoothDevicesService.registerIntentFilters(activity)
    DeviceList(
        activity,
        bluetoothDevicesService,
        bluetoothController
    )
}

