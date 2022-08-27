package com.shirosoftware.sealprogrammingmobile.device.bluetooth

sealed class BluetoothConnectionState {
    object Connecting : BluetoothConnectionState()
    object Connected : BluetoothConnectionState()
    object Disconnected : BluetoothConnectionState()
    object Writing : BluetoothConnectionState()
    class Error(throwable: Throwable) : BluetoothConnectionState()
}
