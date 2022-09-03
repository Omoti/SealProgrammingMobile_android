package com.shirosoftware.sealprogrammingmobile.camera

import android.content.Context
import android.os.Environment
import java.io.File
import javax.inject.Inject

class ImageDataSource @Inject constructor(private val context: Context) {
    fun createImageFile(fileName: String? = null): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            fileName ?: IMAGE_FILE_NAME, /* prefix */
            IMAGE_SUFFIX, /* suffix */
            storageDir /* directory */
        )
    }

    companion object {
        private const val IMAGE_FILE_NAME = "image"
        private const val IMAGE_SUFFIX = ".jpg"
    }
}