package com.shirosoftware.sealprogrammingmobile.device.bluetooth

import com.shirosoftware.sealprogrammingmobile.domain.DeviceConnectionState
import com.shirosoftware.sealprogrammingmobile.domain.DeviceDiscoveryState
import kotlinx.coroutines.flow.Flow

interface BluetoothController {
    val discoveryState: Flow<DeviceDiscoveryState>
    val connectionState: Flow<DeviceConnectionState>

    fun startDiscovery()
    fun cancelDiscovery()
    suspend fun connect(address: String)
    suspend fun disconnect()
    suspend fun write(command: String)
}
