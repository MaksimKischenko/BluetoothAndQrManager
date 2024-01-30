package com.example.bt_dev.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BluetoothSearching
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.bluetoothmodule.ui.theme.EnableBluetoothAdapter
import com.example.bluetoothmodule.ui.theme.Purple40

@Composable
fun SearchDeviceListTitle(

) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Purple40)
            .padding(12.dp),
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 8.dp),
            color = Color.White,
            text = "Поиск новых устройств..."
        )
    }
}