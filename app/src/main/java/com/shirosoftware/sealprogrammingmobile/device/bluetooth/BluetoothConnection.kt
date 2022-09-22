package com.shirosoftware.sealprogrammingmobile.device.bluetooth

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import androidx.annotation.RequiresPermission
import com.shirosoftware.sealprogrammingmobile.domain.DeviceConnectionState
import java.io.IOException
import java.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

class BluetoothConnection {
    private var socket: BluetoothSocket? = null

    private val _state =
        MutableStateFlow<DeviceConnectionState>(DeviceConnectionState.Disconnected)
    val state: StateFlow<DeviceConnectionState> = _state

    @RequiresPermission
    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun connect(device: BluetoothDevice) {
        Log.d(TAG, "Connecting...")

        if (device.bondState == BluetoothDevice.BOND_NONE) {
            Log.d(TAG, "Create bond")
            device.createBond()
        }

        _state.emit(DeviceConnectionState.Connecting)

        withContext(Dispatchers.IO) {
            try {
                socket = device.createInsecureRfcommSocketToServiceRecord(UUID_SERIAL)
                socket?.connect()
                _state.emit(DeviceConnectionState.Connected)
                Log.d(TAG, "Connected")
            } catch (e: IOException) {
                socket?.close()
                socket = null
                _state.emit(DeviceConnectionState.Error(e))
                Log.d(TAG, "Error: ${e.message}")
            }
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun disconnect() {
        withContext(Dispatchers.IO) {
            try {
                socket?.close()
                socket = null
                _state.emit(DeviceConnectionState.Disconnected)
                Log.d(TAG, "Disconnected")
            } catch (e: IOException) {
                socket = null
                _state.emit(DeviceConnectionState.Disconnected)
                Log.d(TAG, "Disconnected: ${e.message}")
            }
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun write(command: String) {
        socket ?: return

        _state.emit(DeviceConnectionState.Writing)

        withContext(Dispatchers.IO) {
            val outputStream = socket?.outputStream
            outputStream?.write(command.toByteArray())

            _state.emit(DeviceConnectionState.Connected)
        }
    }

    companion object {
        private const val TAG = "BluetoothConnection"

        // SPP用固定値
        private val UUID_SERIAL = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    }
}