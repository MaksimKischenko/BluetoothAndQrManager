package com.example.bt_dev.viewmodel


import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bt_dev.models.Device
import com.example.bt_dev.services.BluetoothDevicesService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.CompletableFuture

class SearchDevicesViewModel(private var bluetoothDevicesService: BluetoothDevicesService) :
    ViewModel() {
    fun listenForDevicesPromise(
        loadingIndicatorState: MutableState<Boolean>,
        foundDeviceListState: MutableState<List<Device>>
    ) = viewModelScope.launch {
        bluetoothDevicesService.devicePromise.thenApply { result ->
            loadingIndicatorState.value = false
            foundDeviceListState.value = result
        }
    }

    suspend fun listenForDevicesFlow(
        loadingIndicatorState: MutableState<Boolean>,
        foundDeviceListState: MutableState<List<Device>>
    ) = viewModelScope.launch {
        bluetoothDevicesService.foundDevicesFlow?.collect {
            loadingIndicatorState.value = false
            foundDeviceListState.value = it
        }
    }

    fun refreshState(
        loadingIndicatorState: MutableState<Boolean>,
        foundDeviceListState: MutableState<List<Device>>
    ) = viewModelScope.launch {
        loadingIndicatorState.value = true
        foundDeviceListState.value = emptyList()
        bluetoothDevicesService.devicePromise = CompletableFuture<MutableList<Device>>()
//            bluetoothDevicesService.foundDevicesFlow = null
    }
}
