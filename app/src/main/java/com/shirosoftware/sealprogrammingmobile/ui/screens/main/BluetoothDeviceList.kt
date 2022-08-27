package com.shirosoftware.sealprogrammingmobile.ui.screens.main

import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shirosoftware.sealprogrammingmobile.R
import com.shirosoftware.sealprogrammingmobile.ui.theme.Primary
import com.shirosoftware.sealprogrammingmobile.ui.theme.SealProgrammingMobileTheme

@Composable
fun DeviceList(
    state: BluetoothState,
    connectedDevice: BluetoothDevice?,
    onClickItem: (device: BluetoothDevice) -> Unit,
    onClickDisconnectDevice: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 12.dp)
    ) {
        Divider(
            modifier = Modifier
                .height(8.dp)
                .width(88.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.height(12.dp))

        // 接続痛のデバイス
        connectedDevice?.let {
            ConnectedDeviceItem(
                name = connectedDevice.name,
                onDisconnectClick = onClickDisconnectDevice,
            )
            Divider()
            Spacer(modifier = Modifier.height(12.dp))
        }

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
                        DeviceItem(
                            device.name ?: device.address,
                            device.bondState == BluetoothDevice.BOND_BONDED,
                        ) {
                            onClickItem(device)
                        }
                        Divider()
                    }
                    item {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
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
fun DeviceItem(name: String, isBonded: Boolean, onClick: () -> Unit) {
    ListItem(modifier = Modifier
        .clickable { onClick.invoke() }
        .padding(8.dp),
        icon = {
            if (isBonded) Icon(
                Icons.Default.Bluetooth,
                contentDescription = null
            )
        }) {
        Text(text = name)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ConnectedDeviceItem(name: String, onDisconnectClick: () -> Unit) {
    ListItem(modifier = Modifier
        .padding(8.dp),
        icon = {
            Icon(
                Icons.Default.Bluetooth,
                contentDescription = null,
                tint = Primary,
            )
        },
        trailing = {
            Button(onClick = onDisconnectClick) {
                Text(text = "切断")
            }
        }
    ) {
        Text(text = name, color = Primary)
    }
}

@Preview
@Composable
fun DeviceListPreview_Searching() {
    SealProgrammingMobileTheme {
        DeviceList(state = BluetoothState.Searching, null, {}, {})
    }
}

@Preview
@Composable
fun DeviceListPreview_Find() {
    SealProgrammingMobileTheme {
        DeviceList(
            state = BluetoothState.Found(
                listOf()
            ),
            null,
            {},
            {},
        )
    }
}

@Preview
@Composable
fun DeviceListPreview_Error() {
    SealProgrammingMobileTheme {
        DeviceList(state = BluetoothState.Error(Throwable("エラー")), null, {}, {})
    }
}

@Preview
@Composable
fun DeviceItemPreview() {
    SealProgrammingMobileTheme {
        DeviceItem(name = "device", false) {}
    }
}