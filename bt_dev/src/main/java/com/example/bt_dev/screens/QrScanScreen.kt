package com.example.bt_dev.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
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
import androidx.core.content.ContextCompat
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions


//implementation ("com.journeyapps:zxing-android-embedded:4.3.0")
//https://github.com/journeyapps/zxing-android-embedded
@Composable
fun QrScanScreen(
    context: Context = LocalContext.current,
    scanLauncher: ActivityResultLauncher<ScanOptions> = scanLauncherInit(context),
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
                ContextCompat.startActivity(context, intent, null)
            }
        }
    }
}

fun scan(
    scanLauncher: ActivityResultLauncher<ScanOptions>,
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
