package com.example.bt_dev.services


import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.IntentCompat
import com.example.bt_dev.data.PreferencesManager
import com.example.bt_dev.data.PrefsKeys
import com.example.bt_dev.models.Device
import com.example.bt_dev.util.IntentsProvider
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.CompletableFuture


class BluetoothDevicesService(context: Context, activity: Activity) {
    var btAdapter: BluetoothAdapter? = null
    var pairedDevicesList = mutableListOf<Device>()
    var foundDevicesList = mutableListOf<Device>()
    var devicePromise = CompletableFuture<MutableList<Device>>()
    var foundDevicesFlow: Flow<List<Device>>? = null

    init {
        val bManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        btAdapter = bManager.adapter
        checkPermissions(context, activity)
        registerIntentFilters(activity)
    }

    fun startBtDiscovery() {
        Log.d("MyLog", "startBtDiscovery")
        try {
            val st = btAdapter?.startDiscovery()
            Log.d("MyLog", "Discovery started: ${st.toString()}")
        } catch (e: SecurityException) {
            Log.d("MyLog", "ERROR_START_DISCOVERY: $e")
        }
    }


    @Composable
    fun enableBluetoothAndLoadBluetoothDeviceList(
        context: Context,
        colorState: MutableState<Boolean>,
        devicesState: MutableState<List<Device>>,
    ): ManagedActivityResultLauncher<Intent, ActivityResult> {
        return rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Toast.makeText(context, "Bluetooth включен", Toast.LENGTH_SHORT).show()
                colorState.value = true
                devicesState.value = getBluetoothDeviceList(context)
                pairedDevicesList.addAll(devicesState.value)
            } else {
                Toast.makeText(context, "Bluetooth выключен", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getBluetoothDeviceList(context: Context): List<Device> {
        val tempList = ArrayList<Device>()
        try {
            val pairedDevices: Set<BluetoothDevice>? = btAdapter?.bondedDevices
            pairedDevices?.forEach {
                tempList.add(Device(it, isSelected(context, it.address)))
            }
        } catch (e: SecurityException) {
            tempList.add(Device(null, false))
        }
        return tempList
    }

    private fun isSelected(context: Context, itemMacAddress: String): Boolean {
        return try {
            val preferencesManager = PreferencesManager.getInstance(context)
            val selectedMacAddressName = when {
                preferencesManager.contains(PrefsKeys.selectedDeviceMac.key) ->
                    preferencesManager.read(PrefsKeys.selectedDeviceMac)
                else -> ""
            }
            when (selectedMacAddressName) {
                itemMacAddress -> true
                else -> false
            }
        } catch (e: Exception) {
            false
        }
    }

    private val bReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device = IntentCompat.getParcelableExtra(
                        intent,
                        BluetoothDevice.EXTRA_DEVICE,
                        BluetoothDevice::class.java
                    )
                    if (!pairedDevicesList.map { e -> e.device }.contains(device)
                        && !foundDevicesList.map { e -> e.device }.contains(device)
                    ) {
                        foundDevicesList.add(Device(device, false))
                    }
//                    foundDevicesFlow = flow {
//                        emit(foundDevicesList)
//                    } //flowOf(foundDevicesList)
                    try {
                        Log.d("MyLog", "DEVICE: ${device?.address}")
                    } catch (e: SecurityException) {
                        Log.d("MyLog", "ERROR_GET: ${e}")
                    }
                }

                BluetoothDevice.ACTION_BOND_STATE_CHANGED -> {
                    Log.d("MyLog", "ACTION_BOND_STATE_CHANGED")
                }

                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    foundDevicesList = foundDevicesList.distinctBy { it.device?.address }.toMutableList()
                    devicePromise.complete(foundDevicesList)
                    Log.d("MyLog", "ACTION_DISCOVERY_FINISHED")
                }
            }
        }
    }

    private fun checkPermissions(context: Context, activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_SCAN
                ) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(
                        Manifest.permission.BLUETOOTH_CONNECT,
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ), 1
                )
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ), 1
                )
            }
        }
    }

    private fun registerIntentFilters(activity: Activity) {
        Log.d("MyLog", "registerIntentFilters")
        activity.registerReceiver(bReceiver, IntentsProvider.actionFoundBluetoothDeviceFilter)
        activity.registerReceiver(
            bReceiver,
            IntentsProvider.actionBondStateChangedBluetoothDeviceFilter
        )
        activity.registerReceiver(
            bReceiver,
            IntentsProvider.actionDiscoveryFinishedBluetoothAdapterFilter
        )
    }
}