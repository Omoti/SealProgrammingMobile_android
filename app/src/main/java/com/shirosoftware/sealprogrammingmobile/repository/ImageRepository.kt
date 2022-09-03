package com.shirosoftware.sealprogrammingmobile.repository

import android.graphics.Bitmap
import com.shirosoftware.sealprogrammingmobile.camera.ImageDataSource
import java.io.File
import javax.inject.Inject

class ImageRepository @Inject constructor(private val imageDataSource: ImageDataSource) {
    fun createImageFile(): File {
        return imageDataSource.createImageFile()
    }

    fun getCapturedImage(path: String): Bitmap? {
        return imageDataSource.getCapturedImage(path)
    }
}
