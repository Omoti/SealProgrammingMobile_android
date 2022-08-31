package com.shirosoftware.sealprogrammingmobile.ui.screens.camera

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner

@Composable
fun CameraScreen(
    viewModel: CameraViewModel,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    CameraPreview(lifecycleOwner = lifecycleOwner)
}
