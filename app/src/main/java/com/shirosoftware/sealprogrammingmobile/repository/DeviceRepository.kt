package com.shirosoftware.sealprogrammingmobile.repository

import android.util.Log
import com.shirosoftware.sealprogrammingmobile.device.bluetooth.BluetoothController
import com.shirosoftware.sealprogrammingmobile.domain.Device

class DeviceRepository(
    private val bluetoothController: BluetoothController,
) {
    val discoveryState = bluetoothController.discoveryState
    val connectionState = bluetoothController.connectionState

    val foundDevices = bluetoothController.foundDevices

    fun startDiscovery() {
        Log.d("DeviceRepository", "startDiscovery")
        bluetoothController.startDiscovery()
    }

    fun cancelDiscovery() {
        Log.d("DeviceRepository", "cancelDiscovery")
        bluetoothController.cancelDiscovery()
    }

    suspend fun connect(device: Device) {
        bluetoothController.connect(device.address)
    }

    suspend fun disconnect() {
        bluetoothController.disconnect()
    }

    suspend fun write(command: String) {
        bluetoothController.write(command)
    }
}
