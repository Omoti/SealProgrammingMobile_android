package com.shirosoftware.sealprogrammingmobile.domain

sealed class DeviceDiscoveryState {
    class Found(val devices: List<Device>) : DeviceDiscoveryState()
    object Searching : DeviceDiscoveryState()
    class Error(val throwable: Throwable) : DeviceDiscoveryState()
}
