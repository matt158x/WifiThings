package com.example.wifithings.domain.repository

import com.example.wifithings.data.model.Device
import com.example.wifithings.domain.model.ConnectionState
import com.example.wifithings.domain.model.DeviceState
import kotlinx.coroutines.flow.Flow

interface DeviceRepository {
    suspend fun connect(ipAddress: String): Result<ConnectionState>
    suspend fun sendCommand(command: String): Result<String>
    suspend fun getStatus(): Result<DeviceState>

    suspend fun addDevice(newDevice: Device)
    suspend fun removeDevice(device: Device)
    fun getDevices(): Flow<List<Device>>
}