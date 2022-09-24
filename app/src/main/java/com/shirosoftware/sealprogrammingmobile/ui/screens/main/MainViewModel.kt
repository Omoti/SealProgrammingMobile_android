package com.shirosoftware.sealprogrammingmobile.ui.screens.main

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shirosoftware.sealprogrammingmobile.converter.DetectionResultsConverter
import com.shirosoftware.sealprogrammingmobile.domain.Device
import com.shirosoftware.sealprogrammingmobile.domain.DeviceConnectionState
import com.shirosoftware.sealprogrammingmobile.ml.DetectionResult
import com.shirosoftware.sealprogrammingmobile.repository.DeviceRepository
import com.shirosoftware.sealprogrammingmobile.repository.ImageRepository
import com.shirosoftware.sealprogrammingmobile.repository.SettingsRepository
import com.shirosoftware.sealprogrammingmobile.ui.device.WriteState
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository,
    private val imageRepository: ImageRepository,
    private val settingsRepository: SettingsRepository,
) : ViewModel() {
    private val _bitmap = MutableStateFlow<Bitmap?>(null)
    val bitmap: StateFlow<Bitmap?> = _bitmap

    val discoveryState = deviceRepository.discoveryState

    private val _selectedDevice = MutableStateFlow<Device?>(null)
    val selectedDevice: StateFlow<Device?> = _selectedDevice

    val foundDevices = deviceRepository.foundDevices

    val connectionState = deviceRepository.connectionState

    // 接続経験ありかつ未接続なら自動再接続
    val shouldAutoConnect: Flow<Boolean> =
        combine(
            settingsRepository.lastAddress, connectionState, ::Pair
        ).map { (address: String, state: DeviceConnectionState) ->
            address.isNotEmpty() && state == DeviceConnectionState.Disconnected
        }

    private val _writing = MutableStateFlow<WriteState>(WriteState.Ready)
    val writing: StateFlow<WriteState> = _writing

    private var scanTimer: Timer? = null

    fun loadCapturedImage(path: String) {
        _bitmap.value = imageRepository.getCapturedImage(path)
    }

    fun startSearchDevices() {
        deviceRepository.startDiscovery()

        // 一定時間で自動停止
        scanTimer = Timer()
        scanTimer?.schedule(object : TimerTask() {
            override fun run() {
                deviceRepository.cancelDiscovery()
            }
        }, 10000)
    }

    fun stopSearchDevices() {
        scanTimer?.cancel()
        deviceRepository.cancelDiscovery()
    }

    fun connectToLastDevice() {
        viewModelScope.launch {
            val device =
                foundDevices.first().find { it.address == settingsRepository.lastAddress.first() }

            device?.let {
                connect(device)
            }
        }
    }

    fun connect(device: Device) {
        _selectedDevice.value = device

        viewModelScope.launch {
            deviceRepository.connect(device)
            settingsRepository.updateLastAddress(device.address)
        }
    }

    fun disconnect() {
        _selectedDevice.value = null

        viewModelScope.launch {
            deviceRepository.disconnect()
            settingsRepository.updateLastAddress("")
        }
    }

    fun sendCommand(results: List<DetectionResult>) {
        val commands = DetectionResultsConverter.convertResultsToCommands(results)

        viewModelScope.launch {
            _writing.emit(WriteState.Writing)

            deviceRepository.write(commands)

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
