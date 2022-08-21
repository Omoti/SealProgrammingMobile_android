package com.shirosoftware.sealprogrammingmobile.ui.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shirosoftware.sealprogrammingmobile.R
import com.shirosoftware.sealprogrammingmobile.ui.theme.SealProgrammingMobileTheme

@Composable
fun DeviceList(state: BluetoothState, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
    ) {
        when (state) {
            is BluetoothState.Searching -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = stringResource(id = R.string.bluetooth_searching))
                    Spacer(modifier = Modifier.height(24.dp))
                    CircularProgressIndicator()
                }
            }
            is BluetoothState.Error -> Text(
                text = state.throwable.message ?: stringResource(id = R.string.bluetooth_error)
            )
            is BluetoothState.Found ->
                LazyColumn {
                    items(state.devices.size) { index ->
                        val device = state.devices[index]
                        Text(device.name)
                    }
                }
        }
    }
}

@Preview
@Composable
fun DeviceListPreview_Searching() {
    SealProgrammingMobileTheme {
        DeviceList(state = BluetoothState.Searching)
    }
}

@Preview
@Composable
fun DeviceListPreview_Find() {
    SealProgrammingMobileTheme {
        DeviceList(
            state = BluetoothState.Found(
                listOf()
            )
        )
    }
}

@Preview
@Composable
fun DeviceListPreview_Error() {
    SealProgrammingMobileTheme {
        DeviceList(state = BluetoothState.Error(Throwable("エラー")))
    }
}