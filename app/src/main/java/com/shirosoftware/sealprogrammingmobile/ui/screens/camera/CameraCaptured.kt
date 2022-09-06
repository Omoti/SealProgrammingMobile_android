package com.shirosoftware.sealprogrammingmobile.ui.screens.camera

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.shirosoftware.sealprogrammingmobile.data.SealDetectionResult
import com.shirosoftware.sealprogrammingmobile.ui.components.CircleButton
import com.shirosoftware.sealprogrammingmobile.ui.screens.commands.CommandList
import com.shirosoftware.sealprogrammingmobile.ui.theme.Primary
import java.io.File
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CameraCaptured(
    viewModel: CameraViewModel,
    path: String,
    bitmap: Bitmap,
    modifier: Modifier = Modifier,
    onCanceled: () -> Unit = {},
    onCompleted: (result: SealDetectionResult) -> Unit = {},
) {
    val result = viewModel.result.collectAsState()
    val threshold = viewModel.threshold.collectAsState(0.0f)

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden
    )

    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(path, threshold) {
        Log.d("CameraCaptured", "path: $path, threshold: $threshold")
        viewModel.detectSeals(path)
    }

    DisposableEffect(lifecycleOwner) {
        onDispose {
            Log.d("CameraCaptured", "onDispose")
            bitmap.recycle()
        }
    }

    Log.d("CameraCaptured", "resultImagePath: ${result.value}")

    ModalBottomSheetLayout(
        modifier = modifier,
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp),
        sheetContent = {
            CommandList(detectionResults = result.value?.detectionResults ?: listOf())
        }) {
        Column(
            modifier = Modifier
                .background(Color.Black),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.0f),
                contentAlignment = Alignment.TopCenter,
            ) {
                if (!bitmap.isRecycled) {
                    Image(
                        bitmap.asImageBitmap(),
                        contentDescription = null,
                    )
                }
                Box(
                    modifier = Modifier
                        .aspectRatio(3.0f / 4.0f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.BottomCenter,
                ) {
                    result.value?.let {
                        Image(
                            painter = rememberImagePainter(File(it.imagePath)),
                            contentDescription = null,
                        )
                    } ?: LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth(),
                        color = Primary,
                        trackColor = Color.Black,
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // リスト
                CircleButton(
                    icon = Icons.Default.List,
                    text = "リスト",
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Black,
                    ),
                    onClick = {
                        scope.launch {
                            sheetState.animateTo(ModalBottomSheetValue.Expanded)
                        }
                    },
                    modifier = Modifier
                        .width(100.dp),
                )

                // OK
                CircleButton(
                    icon = Icons.Default.Check,
                    text = "OK",
                    onClick = {
                        result.value?.let {
                            val updatedResult = viewModel.updateResult(it.imagePath)
                            onCompleted.invoke(updatedResult)
                        }
                    },
                    enabled = result.value != null
                )

                // リトライ
                CircleButton(
                    icon = Icons.Default.PhotoCamera,
                    text = "とりなおす",
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Black,
                    ),
                    onClick = onCanceled,
                    modifier = Modifier
                        .width(100.dp),
                )
            }
        }
    }
}