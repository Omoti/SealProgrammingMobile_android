package com.shirosoftware.sealprogrammingmobile.ui.screens.main

import android.bluetooth.BluetoothDevice
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shirosoftware.sealprogrammingmobile.converter.DetectionResultsConverter
import com.shirosoftware.sealprogrammingmobile.device.bluetooth.BluetoothController
import com.shirosoftware.sealprogrammingmobile.ml.DetectionResult
import com.shirosoftware.sealprogrammingmobile.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val bluetoothController: BluetoothController,
    private val imageRepository: ImageRepository,
) : ViewModel() {
    private val _bitmap = MutableStateFlow<Bitmap?>(null)
    val bitmap: StateFlow<Bitmap?> = _bitmap

    private val _bluetoothState = MutableStateFlow<BluetoothState>(BluetoothState.Searching)
    val bluetoothState: StateFlow<BluetoothState> = _bluetoothState

    private val _selectedDevice = MutableStateFlow<BluetoothDevice?>(null)
    val selectedDevice: StateFlow<BluetoothDevice?> = _selectedDevice

    val connectionState = bluetoothController.connectionState

    private val _writing = MutableStateFlow<WriteState>(WriteState.Ready)
    val writing: StateFlow<WriteState> = _writing

    fun loadCapturedImage(path: String) {
        _bitmap.value = imageRepository.getCapturedImage(path)
    }

    fun startSearchDevices() {
        viewModelScope.launch {
            bluetoothController.devices.collect {
                _bluetoothState.value = BluetoothState.Found(it)
            }

            bluetoothController.startDiscovery()
        }
    }

    fun stopSearchDevices() {
        bluetoothController.cancelDiscovery()
        _bluetoothState.value = BluetoothState.Searching
    }

    fun connect(device: BluetoothDevice) {
        _selectedDevice.value = device

        viewModelScope.launch {
            bluetoothController.connect(device)
        }
    }

    fun disconnect() {
        _selectedDevice.value = null

        viewModelScope.launch {
            bluetoothController.disconnect()
        }
    }

    fun sendCommand(results: List<DetectionResult>) {
        val commands = DetectionResultsConverter.convertResultsToCommands(results)

        viewModelScope.launch {
            _writing.emit(WriteState.Writing)

            bluetoothController.write(commands)

            // 演出のためのdelay
            delay(500)

            _writing.emit(WriteState.Completed)
        }
    }

    fun resetWriteState() {
        viewModelScope.launch {
            _writing.emit(WriteState.Ready)
        }
    }
}
