package com.example.bt_dev.screens

import android.content.Context
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.bt_dev.services.BluetoothDevicesService
import com.example.bt_dev.services.QrScanService
import com.journeyapps.barcodescanner.ScanOptions
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

//implementation ("com.journeyapps:zxing-android-embedded:4.3.0")
//https://github.com/journeyapps/zxing-android-embedded
@Composable
fun QrScanScreen(
    scanLauncher: ActivityResultLauncher<ScanOptions>,
    context: Context = LocalContext.current,
    qrScanService: QrScanService = koinInject(parameters = { parametersOf(context)}),
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center

    ) {
        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
                .padding(horizontal = 52.dp),
            border = BorderStroke(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary
            ),
            onClick = {
                scan(scanLauncher)
            }
        ) {
            Text(
                modifier = Modifier.padding(),
                text = "QrCode",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}

fun scan(
    scanLauncher: ActivityResultLauncher<ScanOptions>
) {
    val options = ScanOptions()
    options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
    options.setPrompt("DEVICE MANAGER")
    options.setCameraId(0) // Use a specific camera of the device

    options.setBeepEnabled(false)
    options.setBarcodeImageEnabled(true)
    try {
        scanLauncher.launch(options)
    } catch (e: Exception) {
        Log.d("MyLog", "$e")
    }

}
