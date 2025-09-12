package com.example.wifithings.data.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Device(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val ipAddress: String,
    val type: DeviceType = DeviceType.LED_MIRROR
)

@Serializable
enum class DeviceType {
    LED_MIRROR,

}