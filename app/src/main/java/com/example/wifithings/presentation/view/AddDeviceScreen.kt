package com.example.wifithings.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wifithings.components.CustomTextField
import com.example.wifithings.data.model.Device
import com.example.wifithings.presentation.viewmodel.MainViewModel
import com.example.wifithings.ui.theme.Alkatr


@Composable
fun AddDeviceScreen(
    navController: NavController,
    viewModel: MainViewModel
) {
    var deviceName by remember { mutableStateOf("") }
    var ipAddress by remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF181414)),
        verticalArrangement = Arrangement.Top,
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF181414))
                .padding(top = 80.dp, start = 20.dp)
        ) {
            IconButton(
                onClick = { navController.popBackStack() },

            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Text(
                text = "Dodaj urządzenie",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontFamily = Alkatr,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(bottom = 20.dp),

            )
        }
            Column(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxSize()

            ) {
                 CustomTextField(
                    value = deviceName,
                    onValueChange = { deviceName = it },
                    label = ("Nazwa urządzenia"),
                     fontFamily = Alkatr,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                CustomTextField(
                    value = ipAddress,
                    onValueChange = { ipAddress = it },
                    label = ("Adres IP"),
                    fontFamily = Alkatr,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 50.dp)
                )

                Button(
                    onClick = {
                        if (deviceName.isNotEmpty() && ipAddress.isNotEmpty()) {
                            val newDevice = Device(
                                name = deviceName,
                                ipAddress = ipAddress
                            )
                            viewModel.addDevice(newDevice)
                            navController.popBackStack()
                        }
                    },
                    enabled = deviceName.isNotEmpty() && ipAddress.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50),       // zielony dla enabled
                        disabledContainerColor = Color.Gray, // szary dla disabled
                        contentColor = Color.Black,               // kolor tekstu dla enabled
                        disabledContentColor = Color.Black        // kolor tekstu dla disabled
                    ),
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(horizontal = 30.dp)
                ) {
                    Text("Dodaj urządzenie")

                }

            }
    }
}