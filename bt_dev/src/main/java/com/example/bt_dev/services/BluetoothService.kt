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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.IntentCompat
import com.example.bt_dev.data.PreferencesManager
import com.example.bt_dev.data.PrefsKeys
import com.example.bt_dev.models.Device
import com.example.bt_dev.util.IntentsProvider
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.concurrent.CompletableFuture
import kotlin.random.Random


class BluetoothService {
    var tempDevicesSet = mutableListOf<Device>()
    val promise = CompletableFuture<MutableList<Device>>()
    data class BluetoothServiceResult(val instance: BluetoothService, val adapter: BluetoothAdapter?)
    companion object {
        private var instance: BluetoothService? = null
        private var btAdapter : BluetoothAdapter? = null



        fun getInstanceAndInitAdapter(context: Context, activity: Activity): BluetoothServiceResult {
            if (instance == null) {
                val bManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
                btAdapter = bManager.adapter
                instance =  BluetoothService()
            }
            return BluetoothServiceResult(instance!!, btAdapter)
        }
    }


        fun startBtDiscovery() {
            Log.d("MyLog", "startBtDiscovery")
            try {
               val st =  btAdapter?.startDiscovery()
                Log.d("MyLog", st.toString())
            } catch (e:SecurityException) {
                Log.d("MyLog", "ERROR_START_DISCOVERY: ${e}")
            }
        }


       fun checkPermissions(context: Context, activity: Activity) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN ) != PackageManager.PERMISSION_GRANTED
                    &&  ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED
                    &&  ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT ) != PackageManager.PERMISSION_GRANTED
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
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED
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


        @Composable
        fun enableBluetoothAndLoadBluetoothDeviceList(
            context: Context,
            colorState: MutableState<Boolean>,
            devicesState: MutableState<List<Device>>,
        ) : ManagedActivityResultLauncher<Intent, ActivityResult> {
            return rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    Toast.makeText(context, "Bluetooth включен", Toast.LENGTH_SHORT).show()
                    colorState.value = true
                    devicesState.value = getBluetoothDeviceList(context)
                    tempDevicesSet.addAll(devicesState.value)
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
                tempList.add(Device(null,  false))
            }
            return tempList
        }

        private fun isSelected(context: Context, itemMacAddress: String) : Boolean{
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


        //Один и тот же приемник но есть фильтр
        private val bReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
               if(intent?.action == BluetoothDevice.ACTION_FOUND) {
                   val device = IntentCompat.getParcelableExtra(
                       intent,
                       BluetoothDevice.EXTRA_DEVICE,
                       BluetoothDevice::class.java
                   )

                   Thread {
                       tempDevicesSet.add(Device(device, false))
                       Thread.sleep(10000)
                       promise.complete(tempDevicesSet)
                   }.start()


                   try {
                       Log.d("MyLog", "DEVICE: ${device?.address}")
                       Log.d("MyLog", "PROMISE: ${device?.address}")
                   } catch (e:SecurityException) {
                       Log.d("MyLog", "ERROR_GET: ${e}")
                   }

               } else if(intent?.action == BluetoothDevice.ACTION_BOND_STATE_CHANGED) {
                   Log.d("MyLog", "ACTION_BOND_STATE_CHANGED")
               } else if(intent?.action == BluetoothAdapter.ACTION_DISCOVERY_FINISHED) {
                   Log.d("MyLog", "ACTION_DISCOVERY_FINISHED")
               }
            }
        }


       private fun emitFlowDevices(set: MutableList<Device>): Flow<List<Device>> = flow {
            delay(1000L)
            emit(set.toList())
       }

      fun registerIntentFilters(activity: Activity) {
            Log.d("MyLog", "registerIntentFilters")
            activity.registerReceiver(bReceiver, IntentsProvider.actionFoundBluetoothDeviceFilter)
            activity.registerReceiver(bReceiver, IntentsProvider.actionBondStateChangedBluetoothDeviceFilter)
            activity.registerReceiver(bReceiver, IntentsProvider.actionDiscoveryFinishedBluetoothAdapterFilter)
       }
    }