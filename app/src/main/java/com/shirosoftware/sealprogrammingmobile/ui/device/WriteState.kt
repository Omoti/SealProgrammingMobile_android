package com.shirosoftware.sealprogrammingmobile.ui.device

sealed class WriteState {
    object Ready : WriteState()
    object Completed : WriteState()
    object Writing : WriteState()
    class Error(val throwable: Throwable) : WriteState()
}
