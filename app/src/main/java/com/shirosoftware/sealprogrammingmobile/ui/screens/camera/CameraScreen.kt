package com.shirosoftware.sealprogrammingmobile.ui.screens.camera

import android.util.Log
import android.view.ViewGroup
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat

@Composable
fun CameraScreen(
    viewModel: CameraViewModel,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val configuration = LocalConfiguration.current

    val imageCapture = ImageCapture.Builder()
        .setTargetRotation(configuration.orientation)
        .build()

    Column(modifier = modifier.background(Color.Black)) {
        // プレビュー
        AndroidView(
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(3.0f / 4.0f),
            factory = { context ->

                // プレビュー
                // https://developer.android.com/training/camerax/preview?hl=ja
                val scaleType = PreviewView.ScaleType.FILL_CENTER

                val previewView = PreviewView(context).apply {
                    this.scaleType = scaleType
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                    )
                    implementationMode = PreviewView.ImplementationMode.PERFORMANCE
                }

                val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()

                    val preview: Preview = Preview.Builder()
                        .build()

                    val cameraSelector: CameraSelector = CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build()

                    preview.setSurfaceProvider(previewView.surfaceProvider)

                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            imageCapture,
                            preview,
                        )
                    } catch (e: Exception) {
                        Log.e("CameraScreen", "Use case binding failed", e)
                    }

                }, ContextCompat.getMainExecutor(context))

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
