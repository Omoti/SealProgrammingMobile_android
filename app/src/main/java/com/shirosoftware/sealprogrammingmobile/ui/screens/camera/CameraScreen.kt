package com.shirosoftware.sealprogrammingmobile.ui.screens.camera

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun CameraScreen(
    viewModel: CameraViewModel,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    Column(modifier = modifier.background(Color.Black)) {
        // プレビュー
        CameraPreview(lifecycleOwner = lifecycleOwner, modifier = Modifier.fillMaxWidth())

        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (shutterButton, switchButton) = createRefs()

            ShutterButton(
                modifier = Modifier
                    .size(88.dp)
                    .constrainAs(shutterButton) {
                        centerTo(parent)
                    },
            )
            SwitchCameraButton(
                modifier = Modifier
                    .size(56.dp)
                    .constrainAs(switchButton) {
                        centerVerticallyTo(parent)
                        start.linkTo(shutterButton.end)
                        end.linkTo(parent.end)
                    },
            )
        }
    }
}
