package com.shirosoftware.sealprogrammingmobile.ui.screens.camera

import androidx.lifecycle.ViewModel
import com.shirosoftware.sealprogrammingmobile.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class CameraViewModel @Inject constructor(private val repository: ImageRepository) : ViewModel() {
    private val _state = MutableStateFlow<CameraState>(CameraState.Ready)
    val state: StateFlow<CameraState> = _state

    fun createImageFile(): File {
        return repository.createImageFile()
    }

    fun updateState(state: CameraState) {
        _state.value = state
    }
}
