package com.example.bt_dev.bottom_nav

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.bt_dev.screens.BluetoothScreen
import com.example.bt_dev.screens.QrScanScreen

//implementation ("androidx.navigation:navigation-compose:2.7.6")
@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun NavGraph(
    innerPadding: PaddingValues,
    activity: Activity,
    navHostController: NavHostController,
) {
    NavHost(
        navController = navHostController,
        startDestination = "bluetoothScreen",
        builder = {
            composable("bluetoothScreen") {
                BluetoothScreen(
                    innerPadding,
                    activity
                )
            }
            composable("qRCamScreen") {
                QrScanScreen()
            }
        }
    )
}