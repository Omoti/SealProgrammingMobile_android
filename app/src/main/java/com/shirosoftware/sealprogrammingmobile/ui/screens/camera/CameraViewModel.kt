package com.shirosoftware.sealprogrammingmobile.ui.screens.camera

import androidx.lifecycle.ViewModel
import com.shirosoftware.sealprogrammingmobile.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(private val repository: ImageRepository) : ViewModel() {
    fun createImageFile(): File {
        return repository.createImageFile()
    }
}
