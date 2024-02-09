package com.example.bt_dev.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.bt_dev.models.Device
import com.example.bt_dev.viewmodel.SearchDevicesViewModel

@Composable
fun SearchDeviceListTitle(
    openAlertDialog: MutableState<Boolean>,
    viewModel: SearchDevicesViewModel,
    loadingIndicatorState: MutableState<Boolean>,
    foundDeviceListState: MutableState<List<Device>>
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(12.dp),
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                color = Color.White,
                text = "Поиск новых устройств..."
            )
            Icon(
                modifier = Modifier
                    .clickable {
                        openAlertDialog.value = false
                        viewModel.refreshState(
                            loadingIndicatorState,
                            foundDeviceListState
                        )
                    }
                    .size(24.dp),
                imageVector = Icons.Filled.Close,
                contentDescription = "bluetoothSearching",
                tint = Color.White
            )
        }

    }
}