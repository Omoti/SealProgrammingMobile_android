package com.shirosoftware.sealprogrammingmobile.ui.screens.main

import android.bluetooth.BluetoothDevice

sealed class BluetoothState {
    class Found(val devices: List<BluetoothDevice>) : BluetoothState()
    object Searching : BluetoothState()
    class Error(val throwable: Throwable) : BluetoothState()
}
