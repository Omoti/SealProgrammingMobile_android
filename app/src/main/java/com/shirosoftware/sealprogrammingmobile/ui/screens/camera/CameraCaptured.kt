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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberImagePainter
import com.shirosoftware.sealprogrammingmobile.data.SealDetectionResult
import com.shirosoftware.sealprogrammingmobile.ui.components.CircleButton
import com.shirosoftware.sealprogrammingmobile.ui.theme.Primary
import java.io.File

@Composable
fun CameraCaptured(
    viewModel: CameraViewModel,
    path: String,
    modifier: Modifier = Modifier,
    onCanceled: () -> Unit = {},
    onCompleted: (result: SealDetectionResult) -> Unit = {},
) {
    val context = LocalContext.current
    val result = viewModel.result.collectAsState()
    val threshold = viewModel.threshold.collectAsState(0.0f)

    LaunchedEffect(path, threshold) {
        Log.d("CameraCaptured", "path: $path, threshold: $threshold")
        viewModel.detectSeals(path)
    }
    Log.d("CameraCaptured", "resultImagePath: ${result.value}")
    Column(
        modifier = modifier
            .background(Color.Black)
    ) {
        result.value?.let {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(3.0f / 4.0f),
                painter = rememberImagePainter(File(it.imagePath)),
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
                    result.value?.let {
                        val updatedResult = viewModel.updateResult(it.imagePath)
                        onCompleted.invoke(updatedResult)
                    }
                })
        }
    }
}