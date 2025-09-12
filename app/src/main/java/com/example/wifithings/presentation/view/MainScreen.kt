package com.example.wifithings.presentation.view

import androidx.compose.foundation.background
import androidx.compose.material.icons.filled.ArrowForward
import com.example.wifithings.presentation.viewmodel.MainViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wifithings.data.model.Device
import com.example.wifithings.ui.theme.Alkatr
import com.example.wifithings.ui.theme.Damion
import com.example.wifithings.ui.theme.Submariner

@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel
) {
    val devices by viewModel.devices.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF181414))

    )
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 80.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Moje urządzenia",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontFamily = Alkatr,
                modifier = Modifier.padding(bottom = 24.dp,)
            )
        }

        Column(
            modifier = Modifier
                .padding(20.dp)
        )
        {

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(devices) { device ->
                    DeviceCard(
                        device = device,
                        removeDevice = { viewModel.removeDevice(device) },
                        navigateToDevice = {
                            navController.navigate("deviceControl/${device.id}")
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(50.dp))
                    AddDeviceCard(
                        onClick = {
                            navController.navigate("addDevice")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DeviceCard(
    device: Device,
    removeDevice: () -> Unit,
    navigateToDevice: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = navigateToDevice),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .padding(start = 30.dp, top = 15.dp, bottom = 15.dp, end = 30.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = device.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontFamily = Alkatr,
                )
                Text(
                    text = device.ipAddress,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            IconButton(onClick = removeDevice) {
                Icon(Icons.Default.Delete, contentDescription = "Usuń urządzenie")
            }
        }
    }
}

@Composable
fun AddDeviceCard(
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Dodaj urządzenie",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(30.dp))

            Text(
                text = "Dodaj nowe urządzenie",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}