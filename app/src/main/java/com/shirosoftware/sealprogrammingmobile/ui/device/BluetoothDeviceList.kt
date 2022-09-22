package com.shirosoftware.sealprogrammingmobile.ui.device

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
import androidx.compose.material.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shirosoftware.sealprogrammingmobile.R
import com.shirosoftware.sealprogrammingmobile.domain.Device
import com.shirosoftware.sealprogrammingmobile.domain.DeviceState
import com.shirosoftware.sealprogrammingmobile.ui.theme.Primary
import com.shirosoftware.sealprogrammingmobile.ui.theme.SealProgrammingMobileTheme

@Composable
fun DeviceList(
    state: DeviceState,
    connectedDevice: Device?,
    onClickItem: (device: Device) -> Unit,
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
                mac = connectedDevice.address,
                onDisconnectClick = onClickDisconnectDevice,
            )
            Divider()
            Spacer(modifier = Modifier.height(12.dp))
        }

        when (state) {
            is DeviceState.Searching -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(text = stringResource(id = R.string.bluetooth_searching))
                    Spacer(modifier = Modifier.height(24.dp))
                    CircularProgressIndicator()
                }
            }
            is DeviceState.Error -> Text(
                text = state.throwable.message ?: stringResource(id = R.string.bluetooth_error)
            )
            is DeviceState.Found ->
                LazyColumn {
                    items(state.devices.size) { index ->
                        val device = state.devices[index]
                        DeviceItem(
                            device.name ?: device.address,
                            device.address,
                            device.bonded,
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
fun DeviceItem(name: String, mac: String, isBonded: Boolean, onClick: () -> Unit) {
    ListItem(
        modifier = Modifier
            .padding(8.dp),
        icon = {
            if (isBonded) Icon(
                Icons.Default.Bluetooth,
                contentDescription = null
            )
        },
        secondaryText = { Text(text = mac, color = Color.Gray, fontSize = 12.sp) },
        trailing = {
            Button(onClick = onClick) {
                Text(text = "つなぐ")
            }
        }
    ) {
        Text(text = name)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ConnectedDeviceItem(name: String, mac: String, onDisconnectClick: () -> Unit) {
    ListItem(modifier = Modifier
        .padding(8.dp),
        icon = {
            Icon(
                Icons.Default.Bluetooth,
                contentDescription = null,
                tint = Primary,
            )
        },
        secondaryText = { Text(text = mac, color = Color.Gray, fontSize = 12.sp) },
        trailing = {
            Button(
                onClick = onDisconnectClick,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Red,
                    contentColor = Color.White,
                ),
            ) {
                Text(text = "きる")
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
        DeviceList(state = DeviceState.Searching, null, {}, {})
    }
}

@Preview
@Composable
fun DeviceListPreview_Find() {
    SealProgrammingMobileTheme {
        DeviceList(
            state = DeviceState.Found(
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
        DeviceList(state = DeviceState.Error(Throwable("エラー")), null, {}, {})
    }
}

@Preview
@Composable
fun DeviceItemPreview() {
    SealProgrammingMobileTheme {
        DeviceItem(name = "device", "mac address", false) {}
    }
}