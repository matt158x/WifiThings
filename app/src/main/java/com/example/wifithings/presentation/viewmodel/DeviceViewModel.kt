package com.example.wifithings.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wifithings.domain.model.ConnectionState
import com.example.wifithings.domain.model.DeviceState
import com.example.wifithings.domain.usecase.DeviceUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeviceViewModel @Inject constructor(
    private val useCases: DeviceUseCases
) : ViewModel() {

    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Disconnected)
    val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()

    private val _deviceState = MutableStateFlow<DeviceState>(DeviceState.Unknown)
    val deviceState: StateFlow<DeviceState> = _deviceState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun connect(ipAddress: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _connectionState.value = ConnectionState.Connecting
            _errorMessage.value = null

            useCases.connect(ipAddress).onSuccess { connectionState ->
                _connectionState.value = connectionState
                refreshStatus()
            }.onFailure { error ->
                _connectionState.value = ConnectionState.Error("Connection failed")
                _errorMessage.value = "Błąd: ${error.message ?: "Sprawdź IP i połączenie WiFi"}"
            }
            _isLoading.value = false
        }
    }

    suspend fun turnOn() {
        useCases.turnOn().onSuccess {
            refreshStatus()
        }.onFailure { error ->
            _deviceState.value = DeviceState.Error(error.message ?: "Failed to turn on")
        }
    }

    suspend fun turnOff() {
        useCases.turnOff().onSuccess {
            refreshStatus()
        }.onFailure { error ->
            _deviceState.value = DeviceState.Error(error.message ?: "Failed to turn off")
        }
    }

    suspend fun toggle() {
        useCases.toggle().onSuccess {
            refreshStatus()
        }.onFailure { error ->
            _deviceState.value = DeviceState.Error(error.message ?: "Failed to toggle")
        }
    }

    suspend fun refreshStatus() {
        useCases.getStatus().onSuccess { status ->
            _deviceState.value = status
        }.onFailure { error ->
            _deviceState.value = DeviceState.Error(error.message ?: "Failed to get status")
        }
    }
}