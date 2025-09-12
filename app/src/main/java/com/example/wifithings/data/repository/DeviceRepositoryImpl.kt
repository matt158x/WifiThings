package com.example.wifithings.data.repository

import com.example.wifithings.data.api.DeviceApiService
import com.example.wifithings.data.datastore.AppDataStore
import com.example.wifithings.data.model.Device
import com.example.wifithings.domain.model.ConnectionState
import com.example.wifithings.domain.model.DeviceState
import com.example.wifithings.domain.repository.DeviceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class DeviceRepositoryImpl @Inject constructor(
    private val apiService: DeviceApiService,
    private val dataStore: AppDataStore
) : DeviceRepository {

    private var currentIpAddress: String = ""

    override suspend fun connect(ipAddress: String): Result<ConnectionState> {
        println("🔗 Connecting to IP: $ipAddress")

        return try {
            currentIpAddress = ipAddress
            val result = apiService.sendRequest("http://$ipAddress/status")

            if (result.isSuccess) {
                println("🎉 Connection successful!")
                Result.success(ConnectionState.Connected)
            } else {
                println("⚠️ Connection failed: ${result.exceptionOrNull()?.message}")
                Result.failure(Exception("Connection failed: ${result.exceptionOrNull()?.message}"))
            }
        } catch (e: Exception) {
            println("💥 Connection error: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun sendCommand(command: String): Result<String> {
        println("📤 Sending command: $command")
        return try {
            val result = apiService.sendRequest("http://$currentIpAddress$command")
            if (result.isSuccess) {
                println("✅ Command successful: ${result.getOrNull()}")
            } else {
                println("❌ Command failed: ${result.exceptionOrNull()?.message}")
            }
            result
        } catch (e: Exception) {
            println("💥 Command error: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun getStatus(): Result<DeviceState> {
        return try {
            val result = apiService.sendRequest("http://$currentIpAddress/status")
            if (result.isSuccess) {
                when (result.getOrNull()) {
                    "ON" -> Result.success(DeviceState.On)
                    "OFF" -> Result.success(DeviceState.Off)
                    else -> Result.success(DeviceState.Unknown)
                }
            } else {
                Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addDevice(newDevice: Device) {
        dataStore.addDevice(newDevice)
    }

    override fun getDevices(): Flow<List<Device>> {
        return dataStore.getDevices()
    }

    override suspend fun removeDevice(device: Device) {
        return dataStore.removeDevice(device)
    }
}