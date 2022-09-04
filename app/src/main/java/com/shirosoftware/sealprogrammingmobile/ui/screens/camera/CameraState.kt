package com.shirosoftware.sealprogrammingmobile.ui.screens.camera

sealed interface CameraState {
    object Ready : CameraState
    class Captured(val path: String) : CameraState
}
