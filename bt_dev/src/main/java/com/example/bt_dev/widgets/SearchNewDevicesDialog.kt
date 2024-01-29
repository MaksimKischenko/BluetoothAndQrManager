package com.example.bt_dev.widgets

import android.app.Activity
import android.util.Log
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
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


    val context = LocalContext.current

    val mutableList = remember {
        mutableStateOf<List<Device>>(emptyList())
    }

    val loading = remember { mutableStateOf(mutableList.value.isEmpty()) }
    val devicesPromise = BluetoothService.getInstanceAndInitAdapter(LocalContext.current, activity).instance.promise
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
                            withContext(Dispatchers.IO) {
//                                listenDeviceFlow(mutableList, mutableFlow.value)
                                devicesPromise.thenAccept{
                                    result -> mutableList.value = result
                                    loading.value = false
                                }
                            }
                        }
                    }
                    SearchDeviceListButton()
                    IndeterminateCircularIndicator(loading)
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
                                DeviceItem(
                                    device = mutableList.value[index],
                                    selected = false,
                                    onTap = {

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

@Composable
fun ListenDeviceFlow(
    state: MutableState<List<Device>>,
    devicesFlow: Flow<List<Device>>?
) {
    Log.d("MyLog", "FLOW: ${devicesFlow}")
    devicesFlow?.collectAsState(state)
}