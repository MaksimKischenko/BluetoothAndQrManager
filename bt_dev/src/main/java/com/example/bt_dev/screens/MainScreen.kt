package com.example.bt_dev.screens

import BottomNavigationContainer
import android.app.Activity
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.bt_dev.bottom_nav.NavGraph
import com.journeyapps.barcodescanner.ScanOptions

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun MainScreen(
    activity: Activity,
    scanLauncher: ActivityResultLauncher<ScanOptions>
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationContainer(navController = navController)
        }
    ) {innerPadding->
        NavGraph(
            innerPadding,
            activity,
            navHostController = navController,
            scanLauncher = scanLauncher
        )
    }
}