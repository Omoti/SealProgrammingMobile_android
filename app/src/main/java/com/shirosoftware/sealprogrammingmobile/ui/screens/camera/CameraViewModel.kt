package com.shirosoftware.sealprogrammingmobile.ui.screens.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shirosoftware.sealprogrammingmobile.data.SealDetectionResult
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

    private val _result = MutableStateFlow<SealDetectionResult?>(null)
    val result: StateFlow<SealDetectionResult?> = _result

    val threshold = settingsRepository.threshold

    fun createImageFile(): File {
        return repository.createImageFile()
    }

    fun updateState(state: CameraState) {
        if (state == CameraState.Ready) {
            _result.value = null
        }

        _state.value = state
    }

    fun detectSeals(path: String) {
        _result.value = null
        val bitmap = repository.getCapturedImage(path)

        viewModelScope.launch(Dispatchers.IO) {
            val threshold = settingsRepository.threshold.first()

            bitmap?.let {
                val detectionResults = sealDetector.runObjectDetection(bitmap, threshold)
                sealDetector.drawDetectionResult(bitmap, detectionResults).also {
                    _result.value =
                        SealDetectionResult(
                            repository.saveBitmap(it).absolutePath,
                            detectionResults
                        )
                }
            }
        }
    }

    fun updateResult(path: String): SealDetectionResult {
        return SealDetectionResult(
            repository.updateResult(path).absolutePath,
            _result.value?.detectionResults ?: listOf()
        )
    }
}
