package com.shirosoftware.sealprogrammingmobile.domain

sealed class DeviceDiscoveryState {
    object NotSearching : DeviceDiscoveryState()
    object Searching : DeviceDiscoveryState()
    class Error(val throwable: Throwable) : DeviceDiscoveryState()
}
