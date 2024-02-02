package com.example.bt_dev.widgets

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.bt_dev.models.Device
import com.example.bt_dev.services.BluetoothDevicesService
import com.example.bt_dev.viewmodel.SearchDevicesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf


@Composable
fun SearchNewDevicesDialog(
    context:Context,
    openAlertDialog: MutableState<Boolean>
) {
    val bluetoothDevicesService = koinInject<BluetoothDevicesService>(parameters = { parametersOf(context) })
    val viewModel = koinViewModel<SearchDevicesViewModel>(parameters = { parametersOf(bluetoothDevicesService) })
    val selectedDevicesState = remember { mutableStateOf<List<Device>>(emptyList())}
    val selectedDevices = mutableListOf<Device>()
    val foundDeviceListState = remember { mutableStateOf<List<Device>>(emptyList()) } //bluetoothDevicesService.flow.collectAsState(initial = emptyList())
    val loadingIndicatorState = remember { mutableStateOf(foundDeviceListState.value.isEmpty()) }
    val coroutineScope = rememberCoroutineScope()

    when {
        openAlertDialog.value -> {

            Dialog(
                onDismissRequest = {
                    openAlertDialog.value = false
                    viewModel.refreshState(
                        loadingIndicatorState,
                        foundDeviceListState
                    )
            }) {
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
                                viewModel.listenForDevicesPromise(
                                    loadingIndicatorState,
                                    foundDeviceListState
                                )
                            }
                        }
                    }

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
        LoadingAnimation3()
    }
}


@Composable
fun LoadingAnimation3(
    circleColor: Color = MaterialTheme.colorScheme.primary,
    circleSize: Dp = 12.dp,
    animationDelay: Int = 400,
    initialAlpha: Float = 0.3f
) {

    // 3 circles
    val circles = listOf(
        remember {
            Animatable(initialValue = initialAlpha)
        },
        remember {
            Animatable(initialValue = initialAlpha)
        },
        remember {
            Animatable(initialValue = initialAlpha)
        }
    )

    circles.forEachIndexed { index, animatable ->

        LaunchedEffect(Unit) {

            // Use coroutine delay to sync animations
            delay(timeMillis = (animationDelay / circles.size).toLong() * index)

            animatable.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = animationDelay
                    ),
                    repeatMode = RepeatMode.Reverse
                )
            )
        }
    }

    // container for circles
    Row(
        modifier = Modifier
        //.border(width = 2.dp, color = Color.Magenta)
    ) {

        // adding each circle
        circles.forEachIndexed { index, animatable ->

            // gap between the circles
            if (index != 0) {
                Spacer(modifier = Modifier.width(width = 6.dp))
            }

            Box(
                modifier = Modifier
                    .size(size = circleSize)
                    .clip(shape = CircleShape)
                    .background(
                        color = circleColor
                            .copy(alpha = animatable.value)
                    )
            ) {
            }
        }
    }
}