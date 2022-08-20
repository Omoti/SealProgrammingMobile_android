package com.shirosoftware.sealprogrammingmobile.ml

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import javax.inject.Inject
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.vision.detector.ObjectDetector

class SealDetector @Inject constructor(private val context: Context) {
    /**
     * runObjectDetection(bitmap: Bitmap)
     *      TFLite Object Detection function
     */
    fun runObjectDetection(bitmap: Bitmap): List<DetectionResult> {
        val image = TensorImage.fromBitmap(bitmap)

        val options = ObjectDetector.ObjectDetectorOptions.builder()
            .setScoreThreshold(THRESHOLD)
            .build()
        val detector = ObjectDetector.createFromFileAndOptions(
            context, // the application context
            MODEL_FILE_NAME, // must be same as the filename in assets folder
            options
        )

        val results = detector.detect(image)

        val resultToDisplay = results.map {
            // Get the top-1 category and craft the display text
            val category = it.categories.first()
            val label = category.label
            val score = category.score

            // Create a data object to display the detection result
            DetectionResult(it.boundingBox, label, score)
        }

        return resultToDisplay
    }


    /**
     * drawDetectionResult(bitmap: Bitmap, detectionResults: List<DetectionResult>
     *      Draw a box around each objects and show the object's name.
     */
    fun drawDetectionResult(
        bitmap: Bitmap,
        detectionResults: List<DetectionResult>
    ): Bitmap {
        val outputBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(outputBitmap)
        val pen = Paint()
        pen.textAlign = Paint.Align.LEFT

        detectionResults.forEach {
            val seal = sealByLabel(it.label)

            // draw bounding box
            pen.color = seal.color
            pen.strokeWidth = 8F
            pen.style = Paint.Style.STROKE
            val box = it.boundingBox
            canvas.drawRect(box, pen)

            val tagSize = Rect(0, 0, 0, 0)

            // calculate the right font size
            pen.style = Paint.Style.FILL_AND_STROKE
            pen.color = Color.YELLOW
            pen.strokeWidth = 2F

            pen.textSize = MAX_FONT_SIZE
            pen.getTextBounds(seal.text, 0, seal.text.length, tagSize)
            val fontSize: Float = pen.textSize * box.width() / tagSize.width()

            // adjust the font size so texts are inside the bounding box
            if (fontSize < pen.textSize) pen.textSize = fontSize

            var margin = (box.width() - tagSize.width()) / 2.0F
            if (margin < 0F) margin = 0F
            canvas.drawText(
                seal.text, box.left + margin,
                box.top + tagSize.height().times(1F), pen
            )
        }
        return outputBitmap
    }

    private fun sealByLabel(label: String): Seal {
        return Seal.values().find { it.label == label } ?: Seal.Forward
    }

    companion object {
        private const val MODEL_FILE_NAME = "seals_model.tflite"
        private const val THRESHOLD = 0.5f
        private const val MAX_FONT_SIZE = 48f
    }
}
