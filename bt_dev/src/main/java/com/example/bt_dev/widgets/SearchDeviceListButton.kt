package com.example.bt_dev.widgets

import android.app.Activity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.BluetoothSearching
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.bluetoothmodule.ui.theme.DisableBluetoothAdapter
import com.example.bluetoothmodule.ui.theme.EnableBluetoothAdapter
import com.example.bt_dev.models.Device

@Composable
fun SearchDeviceListButton(

) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        onClick = {}
    ) {
        Text(
            modifier = Modifier.padding(
                horizontal = 8.dp
            ),
            text = "Поиск новых устройств"
        )
        Icon(
            modifier = Modifier.size(20.dp),
            imageVector = Icons.Filled.BluetoothSearching,
            contentDescription = "Сопряженные устройства",
            tint = EnableBluetoothAdapter
        )
    }
}