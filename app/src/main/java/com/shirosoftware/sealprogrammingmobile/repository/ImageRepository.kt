package com.shirosoftware.sealprogrammingmobile.repository

import android.graphics.Bitmap
import com.shirosoftware.sealprogrammingmobile.camera.ImageDataSource
import java.io.File
import javax.inject.Inject

class ImageRepository @Inject constructor(private val imageDataSource: ImageDataSource) {
    fun createImageFile(): File {
        return imageDataSource.createImageFile()
    }

    fun saveBitmap(bitmap: Bitmap): File {
        return imageDataSource.saveBitmap(bitmap)
    }

    fun updateResult(path: String): File {
        return imageDataSource.updateResult(path)
    }

    fun getCapturedImage(path: String): Bitmap? {
        return imageDataSource.getCapturedImage(path)
    }
}
