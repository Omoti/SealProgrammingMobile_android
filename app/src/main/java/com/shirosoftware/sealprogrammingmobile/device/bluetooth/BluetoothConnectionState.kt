package com.shirosoftware.sealprogrammingmobile.device.bluetooth

sealed class BluetoothConnectionState {
    object Connecting : BluetoothConnectionState()
    object Connected : BluetoothConnectionState()
    object Disconnected : BluetoothConnectionState()
    object Ready : BluetoothConnectionState()
    class Error(throwable: Throwable) : BluetoothConnectionState()
}
