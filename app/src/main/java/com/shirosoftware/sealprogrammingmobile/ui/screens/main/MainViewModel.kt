package com.shirosoftware.sealprogrammingmobile.ui.screens.main

import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shirosoftware.sealprogrammingmobile.camera.CameraController
import com.shirosoftware.sealprogrammingmobile.device.bluetooth.BluetoothController
import com.shirosoftware.sealprogrammingmobile.ml.SealDetector
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
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

    fun dispatchTakePicture(): Intent? =
        cameraController.dispatchTakePictureIntent()

    fun loadCapturedImage() {
        _bitmap.value = cameraController.getCapturedImage()

        viewModelScope.launch(Dispatchers.IO) {
            _bitmap.value?.let {
                val result = sealDetector.runObjectDetection(it)
                _bitmap.value = sealDetector.drawDetectionResult(it, result)
            }
        }
    }

    fun initDeviceController() {
        bluetoothController.start()
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
        bluetoothController.connect(device)
    }

    fun disconnect() {
        _selectedDevice.value?.let {
            bluetoothController.disconnect()
        }
    }
}
