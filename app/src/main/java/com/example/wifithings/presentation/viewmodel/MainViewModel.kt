package com.example.wifithings.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wifithings.data.model.Device
import com.example.wifithings.domain.usecase.DeviceUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCases: DeviceUseCases
) : ViewModel() {

    private val _devices = MutableStateFlow<List<Device>>(emptyList())
    val devices: StateFlow<List<Device>> = _devices.asStateFlow()

    init {
        observeDevices()
    }

    private fun observeDevices() {
        viewModelScope.launch {
            useCases.getDevices().collect { list ->
                _devices.value = list
            }
        }
    }

    fun addDevice(device: Device) {
        viewModelScope.launch {
            useCases.addDevice(device)
        }
    }

    fun removeDevice(device: Device) {
        viewModelScope.launch {
            useCases.removeDevice(device)
        }
    }
}