package com.shirosoftware.sealprogrammingmobile.ui.screens.camera

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen(
    viewModel: CameraViewModel,
    modifier: Modifier = Modifier,
    onCaptured: (path: String) -> Unit = {},
    onClickSettings: () -> Unit = {},
    onBack: () -> Unit = {},
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {},
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Black),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        androidx.compose.material3.Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Color.White,
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onClickSettings) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = null,
                            tint = Color.White,
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        when (val _state = state) {
            is CameraState.Ready -> {
                CameraPreview(
                    file = viewModel.createImageFile(),
                    modifier = Modifier.padding(innerPadding),
                    onCaptured = {
                        viewModel.updateState(CameraState.Captured(it))
                    }
                )
            }
            is CameraState.Captured -> {
                CameraCaptured(
                    viewModel = viewModel,
                    path = _state.path,
                    modifier = Modifier.padding(innerPadding),
                    onCanceled = {
                        viewModel.updateState(CameraState.Ready)
                    },
                    onCompleted = {
                        onCaptured.invoke(it)
                    },
                )
            }
        }
    }
}
