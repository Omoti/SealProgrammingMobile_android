package com.shirosoftware.sealprogrammingmobile.ui.screens.camera

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberImagePainter
import com.shirosoftware.sealprogrammingmobile.ui.components.CircleButton
import com.shirosoftware.sealprogrammingmobile.ui.theme.Primary
import java.io.File

@Composable
fun CameraCaptured(
    viewModel: CameraViewModel,
    path: String,
    modifier: Modifier = Modifier,
    onCanceled: () -> Unit = {},
    onCompleted: (path: String) -> Unit = {},
) {
    val capturedImagePath = viewModel.capturedImagePath.collectAsState()

    LaunchedEffect(path) {
        Log.d("CameraCaptured", "path: $path")
        viewModel.detectSeals(path)
    }
    Log.d("CameraCaptured", "resultImagePath: ${capturedImagePath.value}")
    Column(
        modifier = modifier
            .background(Color.Black)
    ) {
        capturedImagePath.value?.let {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(3.0f / 4.0f),
                painter = rememberImagePainter(File(it)),
                contentDescription = null,
            )
        } ?: Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3.0f / 4.0f),
            contentAlignment = Alignment.TopCenter
        ) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth(),
                color = Primary,
                trackColor = Color.Black,
            )
        }
        ConstraintLayout(
            modifier = Modifier.fillMaxSize(),
        ) {
            val (retryButton, okButton) = createRefs()

            CircleButton(
                icon = Icons.Default.PhotoCamera,
                text = "とりなおす",
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Black,
                ),
                onClick = onCanceled,
                modifier = Modifier
                    .constrainAs(retryButton) {
                        centerVerticallyTo(parent)
                        end.linkTo(okButton.start)
                        start.linkTo(parent.start)
                    }
                    .width(100.dp),
            )
            CircleButton(
                icon = Icons.Default.Check,
                text = "OK",
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Black,
                ),
                modifier = Modifier
                    .constrainAs(okButton) {
                        centerTo(parent)
                    }
                    .border(1.dp, Color.White, CircleShape),
                onClick = {
                    capturedImagePath.value?.let {
                        val result = viewModel.updateResult(it)
                        onCompleted.invoke(result.absolutePath)
                    }
                })
        }
    }
}