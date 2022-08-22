package com.shirosoftware.sealprogrammingmobile.ui.screens.main

import android.Manifest
import android.app.Activity.RESULT_OK
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.ElectricCar
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.shirosoftware.sealprogrammingmobile.R
import com.shirosoftware.sealprogrammingmobile.camera.CameraController
import com.shirosoftware.sealprogrammingmobile.device.bluetooth.BluetoothController
import com.shirosoftware.sealprogrammingmobile.ml.SealDetector
import com.shirosoftware.sealprogrammingmobile.ui.components.CircleButton
import com.shirosoftware.sealprogrammingmobile.ui.theme.BackgroundDark
import com.shirosoftware.sealprogrammingmobile.ui.theme.SealProgrammingMobileTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val bitmap = viewModel.bitmap.collectAsState()
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            if (result.resultCode != RESULT_OK) return@rememberLauncherForActivityResult
            viewModel.loadCapturedImage()
        }
    )

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden
    )
    val bluetoothState = viewModel.bluetoothState.collectAsState()

    val device = viewModel.selectedDevice.collectAsState()

    val permissionState =
        rememberMultiplePermissionsState(
            if (Build.VERSION.SDK_INT >= 31) {
                listOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                )
            } else {
                listOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                )
            }
        )
    if (!permissionState.allPermissionsGranted) {
        SideEffect {
            permissionState.launchMultiplePermissionRequest()
        }
    }

    DisposableEffect(viewModel) {
        onDispose {
            viewModel.disconnect()
        }
    }

    LaunchedEffect(permissionState.allPermissionsGranted) {
        viewModel.initDeviceController()
    }

    // デバイス一覧で検索状態を連動
    LaunchedEffect(sheetState.isVisible) {
        if (!permissionState.allPermissionsGranted) return@LaunchedEffect
        if (sheetState.isVisible) {
            viewModel.startSearchDevices()
        } else {
            viewModel.stopSearchDevices()
        }
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            DeviceList(state = bluetoothState.value) { device ->
                scope.launch { sheetState.hide() }
                viewModel.connect(device)
            }
        }
    ) {
        Scaffold(topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) })
        }) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(Color.White),
            ) {
                Box(
                    modifier = Modifier
                        .aspectRatio(3.0f / 4.0f)
                        .fillMaxWidth()
                ) {
                    bitmap.value?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = null,
                        )
                    }
                }
                Divider()
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = device.value?.name ?: "")
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = modifier
                        .fillMaxSize()
                        .background(BackgroundDark)
                ) {
                    CircleButton(
                        Icons.Default.PhotoCamera,
                        stringResource(id = R.string.main_button_camera),
                        onClick = {
                            viewModel.dispatchTakePicture()?.let {
                                launcher.launch(it)
                                viewModel.startSearchDevices()
                            }
                        })
                    CircleButton(
                        Icons.Default.Bluetooth,
                        stringResource(id = R.string.main_button_connect),
                        onClick = {
                            scope.launch { sheetState.show() }
                        })
                    CircleButton(
                        Icons.Default.ElectricCar,
                        stringResource(id = R.string.main_button_send),
                        onClick = { /*TODO*/ })
                }
            }
        }
    }
}


@Composable
fun PermissionRationaleDialog(onDialogResult: () -> Unit) {
    AlertDialog(
        text = { Text("Rationale") },
        onDismissRequest = {},
        confirmButton = {
            TextButton(onClick = onDialogResult) {
                Text("OK")
            }
        }
    )
}

@Preview
@Composable
fun MainScreenPreview() {
    val context = LocalContext.current
    SealProgrammingMobileTheme {
        MainScreen(
            MainViewModel(
                CameraController(context),
                SealDetector(context),
                BluetoothController(context),
            )
        )
    }
}