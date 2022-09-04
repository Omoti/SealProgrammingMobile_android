package com.shirosoftware.sealprogrammingmobile.ui.screens.camera

sealed interface CameraState {
    object Ready : CameraState
    object Captured : CameraState
}
