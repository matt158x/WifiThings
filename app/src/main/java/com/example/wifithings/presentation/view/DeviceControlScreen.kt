package com.example.wifithings.presentation.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.wifithings.data.model.Device
import com.example.wifithings.domain.model.ConnectionState
import com.example.wifithings.domain.model.DeviceState
import com.example.wifithings.presentation.viewmodel.MainViewModel
import com.example.wifithings.presentation.viewmodel.DeviceViewModel
import com.example.wifithings.ui.theme.Alkatr
import kotlinx.coroutines.launch


@Composable
fun DeviceControlScreen(
    navController: NavController,
    deviceId: String,
    mainViewModel: MainViewModel = hiltViewModel(),
    deviceViewModel: DeviceViewModel = hiltViewModel()
) {
    val devices by mainViewModel.devices.collectAsState()
    val device = remember(deviceId) { devices.find { it.id == deviceId } }
    val connectionState by deviceViewModel.connectionState.collectAsState()


    if (device == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Urządzenie nie znalezione",
                fontFamily = Alkatr
            )
        }
        return
    } else {
        if(connectionState == ConnectionState.Disconnected) {
            deviceViewModel.connect(device.ipAddress)
        }
    }

    Scaffold(
        topBar = {
            DeviceControlTopBar(
                deviceName = device.name,
                onBackClick = { navController.popBackStack() }
            )
        },
        containerColor = Color(0xFF181414)
    ) { innerPadding ->
        DeviceControlContent(
            modifier = Modifier.padding(innerPadding),
            device = device,
            deviceViewModel = deviceViewModel
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun DeviceControlTopBar(
    deviceName: String,
    onBackClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = deviceName,
                color = Color.White,
                fontFamily = Alkatr,
                fontSize = 24.sp
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Wróć do listy urządzeń",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(0xFF181414)
        )
    )
}

@Composable
private fun DeviceControlContent(
    modifier: Modifier = Modifier,
    device: Device,
    deviceViewModel: DeviceViewModel
) {
    val connectionState by deviceViewModel.connectionState.collectAsState()
    val deviceState by deviceViewModel.deviceState.collectAsState()
    val isLoading by deviceViewModel.isLoading.collectAsState()
    val errorMessage by deviceViewModel.errorMessage.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Text(
            text = "IP: ${device.ipAddress}",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
        )

        Spacer(Modifier.height(20.dp))

        ConnectionStatus(connectionState = connectionState)

        Spacer(Modifier.height(10.dp))

        DeviceStatus(deviceState = deviceState)


        errorMessage?.let { message ->
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }


        val coroutineScope = rememberCoroutineScope()
        DeviceControlButtons(
            isConnected = connectionState is ConnectionState.Connected,
            isLoading = isLoading,
            onConnect = {
                coroutineScope.launch { deviceViewModel.connect(device.ipAddress) }
            },
            onTurnOn = {
                coroutineScope.launch { deviceViewModel.turnOn() }
            },
            onTurnOff = {
                coroutineScope.launch { deviceViewModel.turnOff() }
            },
            onToggle = {
                coroutineScope.launch { deviceViewModel.toggle() }
            },
            onRefresh = {
                coroutineScope.launch { deviceViewModel.refreshStatus() }
            }
        )
    }
}

@Composable
private fun ConnectionStatus(connectionState: ConnectionState) {
    val (text, color) = when (connectionState) {
        is ConnectionState.Disconnected -> "❌ Niepołączono" to MaterialTheme.colorScheme.error
        is ConnectionState.Connecting -> "⏳ Łączenie..." to MaterialTheme.colorScheme.primary
        is ConnectionState.Connected -> "✅ Połączono" to MaterialTheme.colorScheme.primary
        is ConnectionState.Error -> "❌ Błąd: ${connectionState.message}" to MaterialTheme.colorScheme.error
    }

    Text(
        text = text,
        color = color,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun DeviceStatus(deviceState: DeviceState) {
    val (text, color) = when (deviceState) {
        is DeviceState.Unknown -> "❓ Nieznany stan" to MaterialTheme.colorScheme.onSurfaceVariant
        is DeviceState.On -> "\uD83D\uDD0C WYŁĄCZONE" to MaterialTheme.colorScheme.primary
        is DeviceState.Off -> "\uD83D\uDCA1 WŁĄCZONE" to MaterialTheme.colorScheme.primary
        is DeviceState.Error -> "⚠️ Błąd: ${deviceState.message}" to MaterialTheme.colorScheme.error
    }

    Text(
        text = text,
        color = color,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

@Composable
private fun DeviceControlButtons(
    isConnected: Boolean,
    isLoading: Boolean,
    onConnect: () -> Unit,
    onTurnOn: () -> Unit,
    onTurnOff: () -> Unit,
    onToggle: () -> Unit,
    onRefresh: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .offset(y = (-50).dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (!isConnected) {
            Button(
                onClick = onConnect,
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth(1f)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Połącz z urządzeniem")
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        if (isConnected) {
            Row(
                modifier = Modifier.fillMaxWidth(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onTurnOn,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    )
                ) {
                    Text("Wyłącz")
                }

                Button(
                    onClick = onTurnOff,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF008000),
                        contentColor = Color.White
                    )
                ) {
                    Text("Włącz")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onToggle,
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text("PRZEŁĄCZ")
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onRefresh,
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text("Odśwież status")
            }
        }
    }
}