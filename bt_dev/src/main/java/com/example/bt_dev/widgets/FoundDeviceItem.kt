package com.example.bt_dev.widgets
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.dp

import androidx.compose.ui.unit.sp
import com.example.bluetoothmodule.ui.theme.DeviceMac
import com.example.bluetoothmodule.ui.theme.DeviceName
import com.example.bt_dev.models.Device
import com.example.bt_dev.util.FontProvider

@Composable
fun FoundDeviceItem(device: Device, selected: Boolean, onTap: (item: Device) -> Unit) {
    val provider = FontProvider.getCertificateProvider()
    val font = Font(
        googleFont = GoogleFont("Open Sans"), //Open Sans
        fontProvider = provider,
    )
    val isItemSelected = remember {
        mutableStateOf(selected)
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
                text = name,
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
            checked = isItemSelected.value, //isItemSelected.value,
            onCheckedChange = {
                isItemSelected.value = it
                onTap.invoke(device)
            }
        )
    }
}