package com.shirosoftware.sealprogrammingmobile.device.bluetooth

import com.shirosoftware.sealprogrammingmobile.domain.Device
import com.shirosoftware.sealprogrammingmobile.domain.DeviceConnectionState
import kotlinx.coroutines.flow.Flow

interface BluetoothController {
    val connectionState: Flow<DeviceConnectionState>
    val devices: Flow<List<Device>>

    fun startDiscovery()
    fun cancelDiscovery()
    suspend fun connect(address: String)
    suspend fun disconnect()
    suspend fun write(command: String)
}
