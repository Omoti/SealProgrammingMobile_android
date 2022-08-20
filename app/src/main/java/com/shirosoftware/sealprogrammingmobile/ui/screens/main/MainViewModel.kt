package com.shirosoftware.sealprogrammingmobile.ui.screens.main

import android.content.Intent
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.shirosoftware.sealprogrammingmobile.camera.CameraController
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class MainViewModel @Inject constructor(
    private val cameraController: CameraController
) : ViewModel() {
    private val _bitmap = MutableStateFlow<Bitmap?>(null)
    val bitmap: StateFlow<Bitmap?> = _bitmap

    fun dispatchTakePicture(): Intent? =
        cameraController.dispatchTakePictureIntent()

    fun loadCapturedImage() {
        _bitmap.value = cameraController.getCapturedImage()
    }
}