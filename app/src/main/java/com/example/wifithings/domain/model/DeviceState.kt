package com.example.wifithings.domain.model

sealed class DeviceState {
    data object Unknown : DeviceState()
    data object On : DeviceState()
    data object Off : DeviceState()
    data class Error(val message: String) : DeviceState()
}

sealed class ConnectionState {
    data object Disconnected : ConnectionState()
    data object Connecting : ConnectionState()
    data object Connected : ConnectionState()
    data class Error(val message: String) : ConnectionState()
}