package com.shirosoftware.sealprogrammingmobile.ui.screens.main

import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shirosoftware.sealprogrammingmobile.R
import com.shirosoftware.sealprogrammingmobile.ui.theme.SealProgrammingMobileTheme

@Composable
fun DeviceList(
    state: BluetoothState,
    modifier: Modifier = Modifier,
    onClickItem: (device: BluetoothDevice) -> Unit,
) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 12.dp)
    ) {
        when (state) {
            is BluetoothState.Searching -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
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
                        DeviceItem(device.name ?: device.address) {
                            onClickItem(device)
                        }
                        Divider()
                    }
                    item {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DeviceItem(name: String, onClick: () -> Unit) {
    ListItem(modifier = Modifier
        .clickable { onClick.invoke() }
        .padding(8.dp),
        icon = { Icon(Icons.Default.Bluetooth, contentDescription = null) }) {
        Text(text = name)
    }
}

@Preview
@Composable
fun DeviceListPreview_Searching() {
    SealProgrammingMobileTheme {
        DeviceList(state = BluetoothState.Searching) {}
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
        ) {}
    }
}

@Preview
@Composable
fun DeviceListPreview_Error() {
    SealProgrammingMobileTheme {
        DeviceList(state = BluetoothState.Error(Throwable("エラー"))) {}
    }
}

@Preview
@Composable
fun DeviceItemPreview() {
    SealProgrammingMobileTheme {
        DeviceItem(name = "device") {}
    }
}