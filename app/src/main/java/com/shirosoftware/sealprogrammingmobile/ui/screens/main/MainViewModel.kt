package com.shirosoftware.sealprogrammingmobile.ui.screens.main

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shirosoftware.sealprogrammingmobile.converter.DetectionResultsConverter
import com.shirosoftware.sealprogrammingmobile.domain.Device
import com.shirosoftware.sealprogrammingmobile.domain.DeviceDiscoveryState
import com.shirosoftware.sealprogrammingmobile.ml.DetectionResult
import com.shirosoftware.sealprogrammingmobile.repository.DeviceRepository
import com.shirosoftware.sealprogrammingmobile.repository.ImageRepository
import com.shirosoftware.sealprogrammingmobile.ui.device.WriteState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository,
    private val imageRepository: ImageRepository,
) : ViewModel() {
    private val _bitmap = MutableStateFlow<Bitmap?>(null)
    val bitmap: StateFlow<Bitmap?> = _bitmap

    private val _discoveryState =
        MutableStateFlow<DeviceDiscoveryState>(DeviceDiscoveryState.Searching)
    val discoveryState: StateFlow<DeviceDiscoveryState> = _discoveryState

    private val _selectedDevice = MutableStateFlow<Device?>(null)
    val selectedDevice: StateFlow<Device?> = _selectedDevice

    val connectionState = deviceRepository.connectionState

    private val _writing = MutableStateFlow<WriteState>(WriteState.Ready)
    val writing: StateFlow<WriteState> = _writing

    fun loadCapturedImage(path: String) {
        _bitmap.value = imageRepository.getCapturedImage(path)
    }

    fun startSearchDevices() {
        viewModelScope.launch {
            deviceRepository.devices.collect {
                _discoveryState.value = DeviceDiscoveryState.Found(it)
            }

            deviceRepository.startDiscovery()
        }
    }

    fun stopSearchDevices() {
        deviceRepository.cancelDiscovery()
        _discoveryState.value = DeviceDiscoveryState.Searching
    }

    fun connect(device: Device) {
        _selectedDevice.value = device

        viewModelScope.launch {
            deviceRepository.connect(device)
        }
    }

    fun disconnect() {
        _selectedDevice.value = null

        viewModelScope.launch {
            deviceRepository.disconnect()
        }
    }

    fun sendCommand(results: List<DetectionResult>) {
        val commands = DetectionResultsConverter.convertResultsToCommands(results)

        viewModelScope.launch {
            _writing.emit(WriteState.Writing)

            deviceRepository.write(commands)

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

    override fun onCleared() {
        disconnect()
        super.onCleared()
    }
}
