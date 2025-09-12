package com.example.wifithings.domain.usecase

import com.example.wifithings.data.model.Device
import com.example.wifithings.domain.model.ConnectionState
import com.example.wifithings.domain.model.DeviceState
import com.example.wifithings.domain.repository.DeviceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeviceUseCases @Inject constructor(
    private val repository: DeviceRepository
) {
    suspend fun connect(ipAddress: String): Result<ConnectionState> {
        return repository.connect(ipAddress)
    }

    suspend fun turnOn(): Result<String> {
        return repository.sendCommand("/on")
    }

    suspend fun turnOff(): Result<String> {
        return repository.sendCommand("/off")
    }

    suspend fun toggle(): Result<String> {
        return repository.sendCommand("/toggle")
    }

    suspend fun getStatus(): Result<DeviceState> {
        return repository.getStatus()
    }

    suspend fun addDevice(device: Device) {
        repository.addDevice(device)
    }

    fun getDevices(): Flow<List<Device>> {
        return repository.getDevices()
    }

    suspend fun removeDevice(device: Device) {
        return repository.removeDevice(device)
    }

}