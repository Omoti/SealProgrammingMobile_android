package com.shirosoftware.sealprogrammingmobile.device.bluetooth

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import com.shirosoftware.sealprogrammingmobile.domain.Device
import com.shirosoftware.sealprogrammingmobile.domain.DeviceConnectionState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow

class BleController(private val context: Context) : BluetoothController {
    private val _connectionState =
        MutableStateFlow<DeviceConnectionState>(DeviceConnectionState.Disconnected)
    override val connectionState: Flow<DeviceConnectionState> = _connectionState

    private var currentGatt: BluetoothGatt? = null

    private var scanCallback: ScanCallback? = null
    private val _foundDevices = mutableListOf<BluetoothDevice>()
    override val devices: Flow<List<Device>> = callbackFlow {
        _foundDevices.clear()

        scanCallback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                super.onScanResult(callbackType, result)

                if (result.device.name != null &&
                    _foundDevices.find { it.address == result.device.address } == null
                ) {
                    _foundDevices.add(result.device)
                    trySend(_foundDevices.map { device ->
                        Device(
                            device.name,
                            device.address,
                            device.bondState == BluetoothDevice.BOND_BONDED,
                        )
                    })
                }
            }

            override fun onBatchScanResults(results: List<ScanResult?>?) {
                super.onBatchScanResults(results)
            }
        }

        startDiscovery()

        awaitClose {
            cancelDiscovery()
        }
    }

    private val scanner: BluetoothLeScanner? by lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter.bluetoothLeScanner
    }

    private val connectCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(
            gatt: BluetoothGatt,
            status: Int,
            newState: Int
        ) {
            when (newState) {
                BluetoothProfile.STATE_CONNECTING -> {
                    _connectionState.value = DeviceConnectionState.Connecting
                }
                BluetoothProfile.STATE_CONNECTED -> {
                    currentGatt = gatt
                    _connectionState.value = DeviceConnectionState.Connected
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    currentGatt = null
                    _connectionState.value = DeviceConnectionState.Disconnected
                }
            }
        }
    }

    override fun startDiscovery() {
        // 必要に応じてフィルタ
//        var scanFilter: ScanFilter = ScanFilter.Builder()
//            .build()

        scanner?.startScan(scanCallback)
    }

    override fun cancelDiscovery() {
        scanner?.stopScan(scanCallback)
    }

    override suspend fun connect(address: String) {
        _foundDevices.find {
            it.address == address
        }?.connectGatt(context, true, connectCallback)
    }

    override suspend fun disconnect() {
        currentGatt?.disconnect()
    }

    override suspend fun write(command: String) {
        TODO("Not yet implemented")
    }
}
