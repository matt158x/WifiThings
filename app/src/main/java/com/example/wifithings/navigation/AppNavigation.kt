package com.example.wifithings.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wifithings.presentation.view.AddDeviceScreen
import com.example.wifithings.presentation.view.DeviceControlScreen
import com.example.wifithings.presentation.view.MainScreen
import com.example.wifithings.presentation.viewmodel.MainViewModel

@Composable
fun AppNavigation(
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") {
            MainScreen(
                navController = navController,
                viewModel = mainViewModel
            )
        }
        composable("addDevice") {
            AddDeviceScreen(
                navController = navController,
                viewModel = mainViewModel
            )
        }
        composable("deviceControl/{deviceId}") { backStackEntry ->
            val deviceId = backStackEntry.arguments?.getString("deviceId") ?: ""
            DeviceControlScreen(
                navController = navController,
                deviceId = deviceId,
                mainViewModel = mainViewModel
            )
        }
    }
}