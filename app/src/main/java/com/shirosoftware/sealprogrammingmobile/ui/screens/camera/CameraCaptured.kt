package com.shirosoftware.sealprogrammingmobile.ui.screens.camera

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import coil.compose.rememberImagePainter
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize(),
        ) {
            Button(
                onClick = onCanceled
            ) {
                Text("キャンセル")
            }
            Button(
                onClick = {
                    capturedImagePath.value?.let {
                        val result = viewModel.updateResult(it)
                        onCompleted.invoke(result.absolutePath)
                    }
                }) {
                Text("OK")
            }
        }
    }
}