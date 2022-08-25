package com.shirosoftware.sealprogrammingmobile.device.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.shirosoftware.sealprogrammingmobile.device.SerialCommand
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class BluetoothController(
    private val context: Context,
    private val connection: BluetoothConnection,
) {
    private val bluetoothAdapter: BluetoothAdapter =
        context.getSystemService(BluetoothManager::class.java).adapter
    private val _devices: MutableList<BluetoothDevice> = mutableListOf()

    private lateinit var bluetoothService: BluetoothService

    val connectionState = connection.state

    interface DiscoverCallback {
        fun foundDevice(device: BluetoothDevice)
    }

    private var discoverCallback: DiscoverCallback? = null

    val devices: Flow<List<BluetoothDevice>> = callbackFlow {
        _devices.clear()
//        _devices.addAll(loadPairedDevices().toList())
//        trySend(_devices)

        discoverCallback = object : DiscoverCallback {
            override fun foundDevice(device: BluetoothDevice) {
                trySend(_devices)
            }
        }

        startDiscovery()

        awaitClose {
            cancelDiscovery()
        }
    }

    val isEnabled get() = bluetoothAdapter.isEnabled

    @SuppressLint("MissingPermission")
    private fun loadPairedDevices(): Set<BluetoothDevice> {
        return bluetoothAdapter.bondedDevices
    }

    private fun registerReceiver() {
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        context.registerReceiver(receiver, filter)
    }

    private fun unRegisterReceiver() {
        try {
            context.unregisterReceiver(receiver)
        } catch (e: IllegalArgumentException) {
            // not registered
        }
    }

    fun startDiscovery() {
        registerReceiver()
        bluetoothAdapter.startDiscovery()
    }

    fun cancelDiscovery() {
        unRegisterReceiver()
        bluetoothAdapter.cancelDiscovery()
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private val receiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)

                    // Add list if name is not null.
                    device?.name?.let {
                        val deviceHardwareAddress = device.address // MAC address

                        if (_devices.find { it.address == deviceHardwareAddress } == null) {
                            _devices.add(device)
                            discoverCallback?.foundDevice(device)
                        }
                    }
                }
            }
        }
    }

    fun start() {
        bluetoothService = BluetoothService()
        bluetoothService.registerCallback(object : BluetoothService.IServiceCallback {
            override fun updateBluetoothStatus(
                status: BluetoothService.BLUETOOTH_STATUS?,
                device: BluetoothDevice?
            ) {
                Log.d("BluetoothController", "updateBluetoothStatus")
            }

            override fun onSendBluetoothCommand(command: String?) {
                Log.d("BluetoothController", "onSendBluetoothCommand")
            }

            override fun onReceiveBluetoothCommand(command: String?) {
                Log.d("BluetoothController", "onReceiveBluetoothCommand")
            }
        })
    }

    suspend fun connect(device: BluetoothDevice) {
        if (device.bondState == BluetoothDevice.BOND_NONE) {
            device.createBond()
            return
        }

        connection.connect(device)
    }

    suspend fun disconnect() {
        connection.disconnect()
    }

    suspend fun write(command: String) {
        // 転送中LED点灯
        connection.write(SerialCommand.ledRed100)

        // クリア
        connection.write(SerialCommand.clear)

        // 点滅
        connection.write(SerialCommand.blink)

        // 1文字ずつ送る
        command.map {
            connection.write(it.toString())
        }

        // ゴール
        connection.write(SerialCommand.goal)

        // 転送中LED消灯
        connection.write(SerialCommand.ledRed0)
    }
}