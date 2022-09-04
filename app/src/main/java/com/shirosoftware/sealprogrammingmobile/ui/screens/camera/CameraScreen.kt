package com.shirosoftware.sealprogrammingmobile.ui.screens.camera

import android.content.Context
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun CameraScreen(
    viewModel: CameraViewModel,
    modifier: Modifier = Modifier,
    onCaptured: (path: String) -> Unit = {},
    onBack: () -> Unit = {},
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    // val configuration = LocalConfiguration.current

    val lensFacing = CameraSelector.LENS_FACING_BACK

    val preview = Preview.Builder().build()
    val previewView = remember { PreviewView(context) }
    val imageCapture: ImageCapture = remember { ImageCapture.Builder().build() }
    val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(lensFacing)
        .build()

    LaunchedEffect(lensFacing) {
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageCapture
        )

        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    Column(modifier = modifier.background(Color.Black)) {
        // プレビュー
        AndroidView(
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(3.0f / 4.0f),
            factory = {
                previewView
            },
        )
        
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
                onClick = {
                    val file = viewModel.createImageFile()
                    val outputOptions = ImageCapture.OutputFileOptions
                        .Builder(file)
                        .build()

                    imageCapture.takePicture(outputOptions,
                        ContextCompat.getMainExecutor(context),
                        object : ImageCapture.OnImageSavedCallback {
                            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                Log.d("CameraScreen", "Save a picture: Success")
                                onCaptured.invoke(file.absolutePath)
                            }

                            override fun onError(exception: ImageCaptureException) {
                                Log.e("CameraScreen", "Save a picture: Error", exception)
                            }
                        })
                }
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

private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also { cameraProvider ->
            cameraProvider.addListener({
                continuation.resume(cameraProvider.get())
            }, ContextCompat.getMainExecutor(this))
        }
    }
