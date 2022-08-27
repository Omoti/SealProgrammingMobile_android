package com.shirosoftware.sealprogrammingmobile.ui.screens.main

sealed class WriteState {
    object Ready : WriteState()
    object Completed : WriteState()
    object Writing : WriteState()
    class Error(val throwable: Throwable) : WriteState()
}
