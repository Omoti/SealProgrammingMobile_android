package com.shirosoftware.sealprogrammingmobile.ui.screens.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shirosoftware.sealprogrammingmobile.ml.DetectionResult
import com.shirosoftware.sealprogrammingmobile.ml.SealDetector
import com.shirosoftware.sealprogrammingmobile.repository.ImageRepository
import com.shirosoftware.sealprogrammingmobile.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val sealDetector: SealDetector,
    private val repository: ImageRepository,
    private val settingsRepository: SettingsRepository,
) : ViewModel() {
    private val _state = MutableStateFlow<CameraState>(CameraState.Ready)
    val state: StateFlow<CameraState> = _state

    private val _resultImagePath = MutableStateFlow<String?>(null)
    val resultImagePath: StateFlow<String?> = _resultImagePath

    private var results: List<DetectionResult>? = null

    fun createImageFile(): File {
        return repository.createImageFile()
    }

    fun updateState(state: CameraState) {
        _state.value = state
    }

    fun detectSeals(path: String) {
        val bitmap = repository.getCapturedImage(path)

        viewModelScope.launch(Dispatchers.IO) {
            val threshold = settingsRepository.threshold.first()

            bitmap?.let {
                val detectionResults = sealDetector.runObjectDetection(bitmap, threshold)
                sealDetector.drawDetectionResult(bitmap, detectionResults).also {
                    _resultImagePath.value = repository.saveToResultFile(it).absolutePath
                }

                results = detectionResults
            }
        }
    }
}
