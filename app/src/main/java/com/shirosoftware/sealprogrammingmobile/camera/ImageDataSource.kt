package com.shirosoftware.sealprogrammingmobile.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Environment
import androidx.exifinterface.media.ExifInterface
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject


class ImageDataSource @Inject constructor(private val context: Context) {
    fun createImageFile(): File {
        return File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                .toString() + File.separator + IMAGE_FILE_NAME + IMAGE_SUFFIX
        )
    }
//
//    fun createImageFile(fileName: String? = null): File {
//        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//        return File.createTempFile(
//            fileName ?: IMAGE_FILE_NAME, /* prefix */
//            IMAGE_SUFFIX, /* suffix */
//            storageDir /* directory */
//        )
//    }

    fun saveToResultFile(bitmap: Bitmap): File {
        val result = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                .toString() + File.separator + RESULT_IMAGE_FILE_NAME + IMAGE_SUFFIX
        )

        FileOutputStream(result).use {
            bitmap.compress(CompressFormat.JPEG, 100, it)
        }

        return result
    }

    fun getCapturedImage(path: String): Bitmap? {
        val bmOptions = BitmapFactory.Options().apply {
            // Get the dimensions of the bitmap
            inJustDecodeBounds = true

            BitmapFactory.decodeFile(path, this)

            // Decode the image file into a Bitmap sized to fill the View
            inJustDecodeBounds = false
            inSampleSize = 1
            inMutable = true
        }

        val exifInterface = ExifInterface(path)
        val orientation = exifInterface.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )

        val bitmap = BitmapFactory.decodeFile(path, bmOptions)
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> {
                rotateImage(bitmap, 90f)
            }
            ExifInterface.ORIENTATION_ROTATE_180 -> {
                rotateImage(bitmap, 180f)
            }
            ExifInterface.ORIENTATION_ROTATE_270 -> {
                rotateImage(bitmap, 270f)
            }
            else -> {
                bitmap
            }
        }
    }

    private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        )
    }

    companion object {
        private const val IMAGE_FILE_NAME = "image"
        private const val IMAGE_SUFFIX = ".jpg"
        private const val RESULT_IMAGE_FILE_NAME = "result"
    }
}