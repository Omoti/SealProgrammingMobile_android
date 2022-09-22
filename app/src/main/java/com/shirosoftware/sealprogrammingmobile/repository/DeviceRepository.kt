package com.shirosoftware.sealprogrammingmobile.repository

import com.shirosoftware.sealprogrammingmobile.device.bluetooth.BluetoothController
import com.shirosoftware.sealprogrammingmobile.domain.Device
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class DeviceRepository(
    private val bluetoothController: BluetoothController,
) {
    val devices: Flow<List<Device>> = bluetoothController.devices.map { devices ->
        devices.map { Device(it.name, it.address) }
    }

    val connectionState = bluetoothController.connectionState

    fun startDiscovery() {
        bluetoothController.startDiscovery()
    }

    fun cancelDiscovery() {
        bluetoothController.cancelDiscovery()
    }

    suspend fun connect(device: Device) {
        val bluetoothDevice = bluetoothController.devices.first().find {
            it.address == device.macAddress
        }

        bluetoothDevice?.let {
            bluetoothController.connect(it)
        }
    }

    suspend fun disconnect() {
        bluetoothController.disconnect()
    }

    suspend fun write(command: String) {
        bluetoothController.write(command)
    }
}
