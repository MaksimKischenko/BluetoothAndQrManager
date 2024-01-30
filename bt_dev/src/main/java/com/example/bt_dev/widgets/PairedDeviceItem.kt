package com.example.bt_dev.widgets


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.dp

import androidx.compose.ui.unit.sp
import com.example.bluetoothmodule.ui.theme.DeviceMac
import com.example.bluetoothmodule.ui.theme.DeviceName
import com.example.bt_dev.data.PreferencesManager
import com.example.bt_dev.data.PrefsKeys
import com.example.bt_dev.models.Device
import com.example.bt_dev.services.BluetoothService
import com.example.bt_dev.util.FontProvider

@Composable
fun PairedDeviceItem(device: Device, selected: Boolean, onTap: (item: Device) -> Unit) {
    val provider = FontProvider.getCertificateProvider()
    val font = Font(
        googleFont = GoogleFont("Open Sans"), //Open Sans
        fontProvider = provider,
    )
    val isItemSelected = remember {
        mutableStateOf(device.isSelected)
    }

    val name: String = try {
        device.device?.name ?: ""
    } catch (e: SecurityException) {
        e.toString()
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = name?:"",
                fontSize = 18.sp,
                fontFamily = FontFamily(
                    font
                ),
                color = DeviceName
            )
            Text(
                text = device.device?.address?: "",
                fontSize = 12.sp,
                fontFamily = FontFamily(
                    font
                ),
                color = DeviceMac
            )
        }
        Checkbox(
            checked = selected, //isItemSelected.value,
            onCheckedChange = {
                isItemSelected.value = it
                onTap.invoke(device)
            }
        )
    }
}