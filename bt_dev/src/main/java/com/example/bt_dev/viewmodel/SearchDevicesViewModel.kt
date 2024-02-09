package com.example.bt_dev.viewmodel


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bt_dev.models.Device
import com.example.bt_dev.models.DevicesStateEnum
import com.example.bt_dev.services.BluetoothDevicesService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.CompletableFuture

class SearchDevicesViewModel(private var bluetoothDevicesService: BluetoothDevicesService) :
    ViewModel() {

    private val tempList = ArrayList<Device>()

    @Composable
    fun ListenForDevicesToState(
        devicesState: DevicesStateEnum,
        coroutineScope: CoroutineScope,
        loadingIndicatorState: MutableState<Boolean>,
        foundDeviceListState: MutableState<List<Device>>
    ) {
        LaunchedEffect(Unit) {
            coroutineScope.launch  {
                withContext(Dispatchers.IO) {
                    if(devicesState == DevicesStateEnum.FLOW){
                        repeat(15){
                            delay(800)
                            listenForDevicesFlow(
                                loadingIndicatorState,
                                foundDeviceListState
                            )
                        }
                    } else if(devicesState == DevicesStateEnum.PROMISE){
                        listenForDevicesPromise(
                            loadingIndicatorState,
                            foundDeviceListState
                        )
                    }
                }
            }
        }
    }
    private fun listenForDevicesPromise(
        loadingIndicatorState: MutableState<Boolean>,
        foundDeviceListState: MutableState<List<Device>>
    ) = viewModelScope.launch {
        bluetoothDevicesService.devicePromise.thenApply { result ->
            loadingIndicatorState.value = false
            foundDeviceListState.value = result
        }
    }

    private suspend fun listenForDevicesFlow(
        loadingIndicatorState: MutableState<Boolean>,
        foundDeviceListState: MutableState<List<Device>>
    ) {
        bluetoothDevicesService.foundDevicesFlow?.collect {
            loadingIndicatorState.value = false
            if(!tempList.contains(it)){
                tempList.add(it)
            }
            foundDeviceListState.value = tempList.toList()
        }
    }

    fun refreshState(
        loadingIndicatorState: MutableState<Boolean>,
        foundDeviceListState: MutableState<List<Device>>
    ) = viewModelScope.launch {
        loadingIndicatorState.value = true
        foundDeviceListState.value = emptyList()
        bluetoothDevicesService.devicePromise = CompletableFuture<MutableList<Device>>()
        bluetoothDevicesService.foundDevicesFlow = null
    }
}
