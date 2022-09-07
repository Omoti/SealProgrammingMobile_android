package com.shirosoftware.sealprogrammingmobile.ui.device

import android.bluetooth.BluetoothDevice

sealed class BluetoothState {
    class Found(val devices: List<BluetoothDevice>) : BluetoothState()
    object Searching : BluetoothState()
    class Error(val throwable: Throwable) : BluetoothState()
}
