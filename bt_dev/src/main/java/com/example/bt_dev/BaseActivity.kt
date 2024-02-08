package com.example.bt_dev


import android.os.Build
import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.example.bluetoothmodule.ui.theme.BaseModuleTheme
import com.example.bt_dev.di.appModule
import com.example.bt_dev.screens.MainScreen
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

open class BaseActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startKoin {
            androidLogger()
            modules(appModule)
        }
        setContent {
            BaseModuleTheme {
                MainScreen(this)
            }
        }
    }
}





