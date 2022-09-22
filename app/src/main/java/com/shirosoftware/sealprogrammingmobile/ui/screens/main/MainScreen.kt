package com.shirosoftware.sealprogrammingmobile.ui.screens.main

import android.Manifest
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.BluetoothConnected
import androidx.compose.material.icons.filled.ElectricCar
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.shirosoftware.sealprogrammingmobile.R
import com.shirosoftware.sealprogrammingmobile.camera.ImageDataSource
import com.shirosoftware.sealprogrammingmobile.data.SealDetectionResult
import com.shirosoftware.sealprogrammingmobile.device.bluetooth.BluetoothConnection
import com.shirosoftware.sealprogrammingmobile.device.bluetooth.BluetoothController
import com.shirosoftware.sealprogrammingmobile.domain.DeviceConnectionState
import com.shirosoftware.sealprogrammingmobile.repository.DeviceRepository
import com.shirosoftware.sealprogrammingmobile.repository.ImageRepository
import com.shirosoftware.sealprogrammingmobile.ui.components.CircleButton
import com.shirosoftware.sealprogrammingmobile.ui.components.SegmentedButtons
import com.shirosoftware.sealprogrammingmobile.ui.device.DeviceList
import com.shirosoftware.sealprogrammingmobile.ui.device.WriteState
import com.shirosoftware.sealprogrammingmobile.ui.screens.commands.CommandList
import com.shirosoftware.sealprogrammingmobile.ui.theme.BackgroundDark
import com.shirosoftware.sealprogrammingmobile.ui.theme.Primary
import com.shirosoftware.sealprogrammingmobile.ui.theme.SealProgrammingMobileTheme
import com.shirosoftware.sealprogrammingmobile.ui.theme.Secondary
import com.shirosoftware.sealprogrammingmobile.ui.theme.SecondaryDisable
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalPermissionsApi::class,
    ExperimentalMaterial3Api::class,
)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    detectionResult: SealDetectionResult? = null,
    onClickCamera: () -> Unit = {},
    onClickInfo: () -> Unit = {},
) {
    val toast = Toast.makeText(
        LocalContext.current,
        stringResource(id = R.string.bluetooth_write_completed),
        Toast.LENGTH_SHORT
    )

    val bitmap = viewModel.bitmap.collectAsState()

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden
    )
    val bluetoothState = viewModel.discoveryState.collectAsState()

    val device = viewModel.selectedDevice.collectAsState()
    val connectionState = viewModel.connectionState.collectAsState()
    val writing = viewModel.writing.collectAsState()

    var selectedIndex by remember { mutableStateOf(0) }

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
            } + if (Build.VERSION.SDK_INT <= 28) {
                listOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            } else listOf(
                Manifest.permission.CAMERA
            ),
        )
    if (!permissionState.allPermissionsGranted) {
        SideEffect {
            permissionState.launchMultiplePermissionRequest()
        }
    }

//    DisposableEffect(viewModel) {
//        onDispose {
//            viewModel.disconnect()
//        }
//    }

    // デバイス一覧で検索状態を連動
    LaunchedEffect(sheetState.isVisible) {
        if (!permissionState.allPermissionsGranted) return@LaunchedEffect
        if (sheetState.isVisible) {
            viewModel.startSearchDevices()
        } else {
            viewModel.stopSearchDevices()
        }
    }

    LaunchedEffect(connectionState.value) {
        Log.d("MainScreen", connectionState.value.toString())
    }

    // 送信完了
    LaunchedEffect(writing.value) {
        if (writing.value == WriteState.Completed) {
            toast.show()
            viewModel.resetWriteState()
        }
    }

    LaunchedEffect(detectionResult) {
        Log.d("MainScreen", "imagePath: ${detectionResult?.imagePath}")
        detectionResult?.let { viewModel.loadCapturedImage(detectionResult.imagePath) }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        color = Color.White,
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Primary),
                actions = {
                    IconButton(onClick = onClickInfo) {
                        androidx.compose.material.Icon(
                            Icons.Outlined.Info,
                            contentDescription = null,
                            tint = Color.White,
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        ModalBottomSheetLayout(
            modifier = modifier.padding(innerPadding),
            sheetState = sheetState,
            sheetShape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp),
            sheetContent = {
                DeviceList(
                    state = bluetoothState.value,
                    connectedDevice = device.value,
                    onClickItem = { device ->
                        scope.launch {
                            sheetState.hide()
                            viewModel.connect(device)
                        }
                    },
                    onClickDisconnectDevice = {
                        scope.launch {
                            sheetState.hide()
                            viewModel.disconnect()
                        }
                    }
                )
            },
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(BackgroundDark),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (bitmap.value != null) {
                    SegmentedButtons(
                        labels = listOf("シール", "リスト"),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 32.dp, top = 8.dp, end = 32.dp, bottom = 8.dp)
                            .height(24.dp),
                    ) {
                        selectedIndex = it
                    }
                } else {
                    Spacer(
                        modifier = Modifier
                            .padding(start = 32.dp, top = 8.dp, end = 32.dp, bottom = 8.dp)
                            .height(24.dp)
                    )
                }
                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .fillMaxWidth()
                        .weight(1.0f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .aspectRatio(3.0f / 4.0f)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center,
                    ) {
                        bitmap.value?.let {
                            if (selectedIndex == 0) {
                                Image(
                                    bitmap = it.asImageBitmap(),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxHeight()
                                )
                            } else {
                                detectionResult?.detectionResults?.let {
                                    CommandList(detectionResults = it)
                                }
                            }
                        } ?: Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White)
                                .padding(24.dp)
                        ) {
                            Text(
                                text = stringResource(id = R.string.main_welcome_title),
                                fontSize = 20.sp,
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = stringResource(id = R.string.main_welcome_content),
                                fontSize = 16.sp,
                            )
                            Spacer(modifier = Modifier.height(32.dp))
                            Image(
                                painter = painterResource(id = R.drawable.app_logo),
                                contentDescription = null,
                                contentScale = ContentScale.FillWidth,
                                alignment = Alignment.TopCenter,
                                modifier = Modifier.width(200.dp)
                            )
                        }
                    }
                    Spacer(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(BackgroundDark)
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = modifier
                        .fillMaxWidth()
                        .height(144.dp)
                        .background(BackgroundDark)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CircleButton(
                            Icons.Default.PhotoCamera,
                            stringResource(id = R.string.main_button_camera),
                            onClick = {
                                onClickCamera.invoke()
                            })
                        CircleButton(
                            if (connectionState.value == DeviceConnectionState.Connected) {
                                Icons.Default.BluetoothConnected
                            } else {
                                Icons.Default.Bluetooth
                            },
                            stringResource(id = R.string.main_button_connect),
                            onClick = {
                                scope.launch {
                                    sheetState.show()
                                }
                                viewModel.startSearchDevices()
                            },
                        )
                        CircleButton(
                            Icons.Default.ElectricCar,
                            stringResource(id = R.string.main_button_send),
                            onClick = {
                                detectionResult?.let { viewModel.sendCommand(it.detectionResults) }
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Secondary,
                                disabledBackgroundColor = SecondaryDisable,
                            ),
                            enabled = (detectionResult != null
                                    && connectionState.value == DeviceConnectionState.Connected)
                                    && writing.value != WriteState.Writing
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = device.value?.name?.let {
                            "$it : ${
                                if (writing.value == WriteState.Writing) stringResource(id = R.string.bluetooth_state_writing)
                                else
                                    when (connectionState.value) {
                                        DeviceConnectionState.Connected -> stringResource(id = R.string.bluetooth_state_connected)
                                        DeviceConnectionState.Connecting -> stringResource(id = R.string.bluetooth_state_connecting)
                                        DeviceConnectionState.Disconnected -> stringResource(id = R.string.bluetooth_state_disconnected)
                                        DeviceConnectionState.Writing -> stringResource(id = R.string.bluetooth_state_writing)
                                        is DeviceConnectionState.Error -> stringResource(id = R.string.bluetooth_state_error)
                                    }
                            }"
                        } ?: "つないでいません",
                        textAlign = TextAlign.Center,
                        style = TextStyle(fontSize = 12.sp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(BackgroundDark),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    val context = LocalContext.current
    SealProgrammingMobileTheme {
        MainScreen(
            MainViewModel(
                DeviceRepository(BluetoothController(context, BluetoothConnection())),
                ImageRepository(ImageDataSource((context))),
            )
        )
    }
}
