package com.shirosoftware.sealprogrammingmobile.device.bluetooth

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException
import java.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

class BluetoothConnection {
    private var socket: BluetoothSocket? = null

    private val _state =
        MutableStateFlow<BluetoothConnectionState>(BluetoothConnectionState.Disconnected)
    val state: StateFlow<BluetoothConnectionState> = _state

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun connect(device: BluetoothDevice) {
        Log.d(TAG, "Connecting...")
        _state.emit(BluetoothConnectionState.Connecting)

        withContext(Dispatchers.IO) {
            try {
                socket = device.createInsecureRfcommSocketToServiceRecord(UUID_SERIAL)
                socket?.connect()
                _state.emit(BluetoothConnectionState.Connected)
                Log.d(TAG, "Connected")
            } catch (e: IOException) {
                socket?.close()
                socket = null
                _state.emit(BluetoothConnectionState.Error(e))
                Log.d(TAG, "Error: ${e.message}")
            }
        }
    }


    suspend fun disconnect() {
        withContext(Dispatchers.IO) {
            try {
                socket?.close()
                socket = null
                _state.emit(BluetoothConnectionState.Disconnected)
                Log.d(TAG, "Disconnected")
            } catch (e: IOException) {
                socket = null
                _state.emit(BluetoothConnectionState.Disconnected)
                Log.d(TAG, "Disconnected: ${e.message}")
            }
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun write(command: String) {
        socket ?: return

        _state.emit(BluetoothConnectionState.Writing)

        withContext(Dispatchers.IO) {
            val outputStream = socket?.outputStream
            outputStream?.write(command.toByteArray())

            // 演出のためのdelay
            delay(500)

            _state.emit(BluetoothConnectionState.WriteCompleted)
        }
    }

    companion object {
        private const val TAG = "BluetoothConnection"

        // SPP用固定値
        private val UUID_SERIAL = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    }
}