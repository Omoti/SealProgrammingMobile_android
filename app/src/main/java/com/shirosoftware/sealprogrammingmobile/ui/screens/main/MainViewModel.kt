package com.shirosoftware.sealprogrammingmobile.ui.screens.main

import android.content.Intent
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.shirosoftware.sealprogrammingmobile.camera.CameraController
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val cameraController: CameraController
) : ViewModel() {
    fun dispatchTakePicture(): Intent? =
        cameraController.dispatchTakePictureIntent()

    fun loadCapturedImage(): Bitmap =
        cameraController.getCapturedImage()
}