package com.shirosoftware.sealprogrammingmobile.domain

sealed class DeviceConnectionState {
    object Connecting : DeviceConnectionState()
    object Connected : DeviceConnectionState()
    object Disconnected : DeviceConnectionState()
    object Writing : DeviceConnectionState()
    class Error(throwable: Throwable) : DeviceConnectionState()
}
