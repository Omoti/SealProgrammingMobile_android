package com.shirosoftware.sealprogrammingmobile.ui.screens.camera

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CameraScreen(
    viewModel: CameraViewModel,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
) {
    Text(text = "Camera")
}
