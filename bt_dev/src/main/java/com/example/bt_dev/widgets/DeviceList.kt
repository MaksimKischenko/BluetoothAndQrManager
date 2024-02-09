package com.example.bt_dev.widgets

import android.app.Activity
import android.content.Context
import android.location.LocationManager
import android.os.Build

import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.bt_dev.data.PreferencesManager
import com.example.bt_dev.data.PrefsKeys
import com.example.bt_dev.models.Device
import com.example.bt_dev.services.BluetoothController
import com.example.bt_dev.services.BluetoothDevicesService
import com.example.bt_dev.util.IntentsProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun DeviceList(
    innerPadding: PaddingValues,
    activity: Activity,
    bluetoothDevicesService: BluetoothDevicesService,
    bluetoothController: BluetoothController
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val deviceState = remember {
        mutableStateOf<List<Device>>(emptyList())
    }
    val preferencesManager = PreferencesManager.getInstance(context)
    val selectedDevice = remember {
        mutableStateOf(Device(device = null, false))
    }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val openDialog = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    Scaffold(
        modifier = Modifier.padding(innerPadding),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        "Поиск устройств",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    Icon(
                        modifier = Modifier
                            .clickable {
                                checkLocationSettings(
                                    context,
                                    activity,
                                    scope,
                                    snackbarHostState,
                                    openDialog,
                                    bluetoothDevicesService
                                )
                            }
                            .size(24.dp),
                        imageVector = Icons.Filled.Devices,
                        contentDescription = "bluetoothSearching",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                scrollBehavior = scrollBehavior,
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            DeviceListButton(
                deviceState,
                activity,
                snackbarHostState,
                bluetoothDevicesService
            )
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(count = deviceState.value.count()) { index ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 6.dp
                        ),
                    ) {
                        val selected = when {
                            deviceState.value[index].isSelected -> true
                            else -> selectedDevice.value == deviceState.value[index]
                        }
                        PairedDeviceItem(
                            device = deviceState.value[index],
                            selected = selected,
                            onTap = {
                                val result = it.device?.let { value -> bluetoothController.connect(value.address) }
                                deviceState.value = deviceState.value.map { device ->
                                    device.copy(isSelected = false)
                                }
                                selectedDevice.value = it
                                preferencesManager.write(
                                    PrefsKeys.selectedDeviceMac,
                                    selectedDevice.value.device?.address
                                )

                            }
                        )
                    }
                }
            }
        }
        SearchNewDevicesDialog(
            openAlertDialog = openDialog
        )
    }
}


private fun checkLocationSettings(
    context: Context,
    activity: Activity,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    openDialog: MutableState<Boolean>,
    bluetoothService: BluetoothDevicesService
) {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
        scope.launch {
            val result = snackbarHostState
                .showSnackbar(
                    message = "Включите геолокацию",
                    duration = SnackbarDuration.Long,
                    actionLabel = "Да",
                    withDismissAction = true
                )
            when (result) {
                SnackbarResult.ActionPerformed -> {
                    activity.startActivity(IntentsProvider.actionRequestEnableLocationSettings)
                }

                SnackbarResult.Dismissed -> {}
            }
        }

    } else {
        bluetoothService.startBtDiscovery()
        openDialog.value = true
    }
}
