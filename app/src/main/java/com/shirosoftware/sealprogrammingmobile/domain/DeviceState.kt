package com.shirosoftware.sealprogrammingmobile.domain

sealed class DeviceState {
    class Found(val devices: List<Device>) : DeviceState()
    object Searching : DeviceState()
    class Error(val throwable: Throwable) : DeviceState()
}
