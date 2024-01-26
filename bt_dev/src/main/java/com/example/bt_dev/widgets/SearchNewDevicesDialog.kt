package com.example.bt_dev.widgets

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.bluetoothmodule.ui.theme.Purple40
import com.example.bt_dev.data.PrefsKeys
import com.example.bt_dev.models.Device
import com.example.bt_dev.services.BluetoothService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun SearchNewDevicesDialog(
    activity: Activity,
    openAlertDialog: MutableState<Boolean>
) {

    val mutableList = remember {
        mutableStateOf<List<Device>>(emptyList())
    }
    val devicesFlow = BluetoothService.deviceFlow
    val coroutineScope = rememberCoroutineScope()

    when {
        openAlertDialog.value -> {

            Dialog(onDismissRequest = { openAlertDialog.value = false }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    LaunchedEffect(Unit) {
                        coroutineScope.launch  {
                            withContext(Dispatchers.Default) {
                                listenDeviceFlow(mutableList, devicesFlow)
                            }
                        }
                    }
                    SearchDeviceListButton()
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        items(count = mutableList.value.count()) { index ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 6.dp
                                ),
                            ) {
                                Text(text = mutableList.value[index].device?.address?:"")
//                                DeviceItem(
//                                    device = deviceState.value[index],
//                                    selected = selected,
//                                    onTap = {
//                                        deviceState.value = deviceState.value.map {
//                                                device -> device.copy(isSelected = false)
//                                        }
//                                        selectedDevice.value = it
//                                        preferencesManager.write(PrefsKeys.selectedDeviceMac, selectedDevice.value.mac)
//                                    }
//                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

suspend fun listenDeviceFlow(
    state: MutableState<List<Device>>,
    devicesFlow: Flow<List<Device>>?
) {
    val list = ArrayList<Device>()
    Log.d("MyLog", "FLOW: ${devicesFlow}")
    devicesFlow?.collect {
        Log.d("MyLog", "FLOW: ${it}")
        state.value = it
    }
}