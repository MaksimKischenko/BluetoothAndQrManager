package com.example.bt_dev.widgets


import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.bluetoothmodule.ui.theme.DisableBluetoothAdapter
import com.example.bluetoothmodule.ui.theme.EnableBluetoothAdapter
import com.example.bt_dev.models.Device
import com.example.bt_dev.services.BluetoothDevicesService
import com.example.bt_dev.util.IntentsProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun DeviceListButton(
    devicesState: MutableState<List<Device>>,
    activity: Activity,
    snackbarHostState: SnackbarHostState,
    bluetoothService: BluetoothDevicesService
) {
    val scope = rememberCoroutineScope()
    val bluetoothDevicesService = BluetoothDevicesService.getInstanceAndInitAdapter(LocalContext.current, activity);
    val colorState = remember {
        mutableStateOf(bluetoothDevicesService.adapter?.isEnabled ?: false)
    }
    val launcher = bluetoothDevicesService.instance.enableBluetoothAndLoadBluetoothDeviceList(
        context = LocalContext.current,
        colorState = colorState,
        devicesState
    )
    OutlinedButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp)
            .padding(horizontal = 52.dp),
        border = BorderStroke(
            width = 2.dp,
            color = btStatusColor(colorState)
        ),
        onClick = {
            onClick(
                launcher,
                scope,
                snackbarHostState
            )
        }
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.padding(),
                text = "Сопряженные устройства",
                style = TextStyle(
                    color = btStatusColor(colorState)
                )
            )
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = Icons.Filled.Bluetooth,
                contentDescription = "Сопряженные устройства",
                tint = btStatusColor(colorState)
            )
        }
    }
}


private fun btStatusColor(
    iconColor: MutableState<Boolean>
): Color {
    return when {
        iconColor.value -> EnableBluetoothAdapter
        else -> DisableBluetoothAdapter
    }
}

private fun onClick(
    launcher: ManagedActivityResultLauncher<Intent, ActivityResult>,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
    try {
        launcher.launch(IntentsProvider.actionRequestEnableBluetoothAdapter)
    } catch (e: SecurityException) {
        scope.launch {
            val result = snackbarHostState
                .showSnackbar(
                    message = e.toString(),
                    duration = SnackbarDuration.Long
                )
            when (result) {
                SnackbarResult.ActionPerformed -> {

                }

                SnackbarResult.Dismissed -> {

                }
            }
        }
    }
}