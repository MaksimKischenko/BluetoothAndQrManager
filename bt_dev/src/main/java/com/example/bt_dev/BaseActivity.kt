package com.example.bt_dev

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat.startActivity
import com.example.bluetoothmodule.ui.theme.BaseModuleTheme
import com.example.bt_dev.di.appModule
import com.example.bt_dev.screens.MainScreen
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
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
                MainScreen(this, scanLauncherInit(this))
            }
        }
    }
}


@Composable
fun scanLauncherInit(context: Context): ManagedActivityResultLauncher<ScanOptions, ScanIntentResult> {
    return rememberLauncherForActivityResult(ScanContract()) { result ->
        if(result.contents == null) {
            Toast.makeText(context, "Scan Data: NULL", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, "Scan Data: ${result.contents}", Toast.LENGTH_LONG).show()
            if (result.contents.startsWith("http://") || result.contents.startsWith("https://")) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(result.contents)
                startActivity(context, intent, null)
            }
        }
    }
}





