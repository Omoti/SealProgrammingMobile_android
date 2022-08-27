package com.shirosoftware.sealprogrammingmobile.ui.screens.main

import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shirosoftware.sealprogrammingmobile.camera.CameraController
import com.shirosoftware.sealprogrammingmobile.converter.DetectionResultsConverter
import com.shirosoftware.sealprogrammingmobile.device.bluetooth.BluetoothController
import com.shirosoftware.sealprogrammingmobile.ml.DetectionResult
import com.shirosoftware.sealprogrammingmobile.ml.SealDetector
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val cameraController: CameraController,
    private val sealDetector: SealDetector,
    private val bluetoothController: BluetoothController,
) : ViewModel() {
    private val _bitmap = MutableStateFlow<Bitmap?>(null)
    val bitmap: StateFlow<Bitmap?> = _bitmap

    private val _bluetoothState = MutableStateFlow<BluetoothState>(BluetoothState.Searching)
    val bluetoothState: StateFlow<BluetoothState> = _bluetoothState

    private val _selectedDevice = MutableStateFlow<BluetoothDevice?>(null)
    val selectedDevice: StateFlow<BluetoothDevice?> = _selectedDevice

    private var results: List<DetectionResult>? = null

    val connectionState = bluetoothController.connectionState

    private val _writing = MutableStateFlow(false)
    val writing: StateFlow<Boolean> = _writing

    fun dispatchTakePicture(): Intent? =
        cameraController.dispatchTakePictureIntent()

    fun loadCapturedImage() {
        _bitmap.value = cameraController.getCapturedImage()

        viewModelScope.launch(Dispatchers.IO) {
            _bitmap.value?.let { bitmap ->
                val detectionResults = sealDetector.runObjectDetection(bitmap)
                _bitmap.value = sealDetector.drawDetectionResult(bitmap, detectionResults)
                results = detectionResults
            }
        }
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

    fun sendCommand() {
        results?.let {
            val commands = DetectionResultsConverter.convertResultsToCommands(it)

            viewModelScope.launch {
                _writing.emit(true)

                bluetoothController.write(commands)

                // 演出のためのdelay
                delay(500)

                _writing.emit(false)
            }
        }
    }
}
