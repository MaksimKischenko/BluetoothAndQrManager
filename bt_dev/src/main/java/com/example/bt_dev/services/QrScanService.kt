package com.example.bt_dev.services

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import com.example.bt_dev.BaseActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions

class QrScanService(val context: Context) {
    @Composable
    fun scanLauncherInit(): ManagedActivityResultLauncher<ScanOptions, ScanIntentResult> {
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
}

