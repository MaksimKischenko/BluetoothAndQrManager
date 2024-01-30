package com.example.bt_dev.widgets

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.bt_dev.models.Device
import com.example.bt_dev.services.BluetoothDevicesService


@Composable
fun SearchNewDevicesDialog(
    activity: Activity,
    openAlertDialog: MutableState<Boolean>
) {

    val foundDeviceListState = remember {
        mutableStateOf<List<Device>>(emptyList())
    }
    val selectedDevicesState = remember {
        mutableStateOf<List<Device>>(emptyList())
    }
    val selectedDevices = mutableListOf<Device>()
    val loadingIndicatorState = remember { mutableStateOf(foundDeviceListState.value.isEmpty()) }
    val devicesPromise = BluetoothDevicesService.getInstanceAndInitAdapter(LocalContext.current, activity).instance.promise
    val deviceAdapter = BluetoothDevicesService.getInstanceAndInitAdapter(LocalContext.current, activity).adapter
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
                    devicesPromise.thenAccept{
                            result -> foundDeviceListState.value = result
                        loadingIndicatorState.value = false
                    }
//                    LaunchedEffect(Unit) {
//                        coroutineScope.launch  {
//                            withContext(Dispatchers.IO) {
//                                devicesPromise.thenAccept{
//                                    result -> foundDeviceListState.value = result
//                                    loadingIndicatorState.value = false
//                                }
//                            }
//                        }
//                    }
                    SearchDeviceListTitle()
                    IndeterminateCircularIndicator(loadingIndicatorState)
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(count = foundDeviceListState.value.count()) { index ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 6.dp
                                ),
                            ) {
                                FoundDeviceItem(
                                    device = foundDeviceListState.value[index],
                                    selected = false, //selectedDevicesState.value.contains(foundDeviceListState.value[index]),
                                    onTap = {

                                        if(!selectedDevices.contains(it)) {
                                            selectedDevices.add(it)
                                            try {
                                                it.device?.createBond()
                                            } catch (e:SecurityException) {

                                            }
                                        } else {
                                            selectedDevices.remove(it)
                                        }
                                        selectedDevicesState.value = selectedDevices
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun IndeterminateCircularIndicator(
    loading: MutableState<Boolean>
) {
    if (!loading.value) return

    Box(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .width(42.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}
