package com.shirosoftware.sealprogrammingmobile.ui.screens.camera

import android.graphics.Bitmap

sealed interface CameraState {
    object Ready : CameraState
    class Captured(val path: String, val bitmap: Bitmap) : CameraState
}
