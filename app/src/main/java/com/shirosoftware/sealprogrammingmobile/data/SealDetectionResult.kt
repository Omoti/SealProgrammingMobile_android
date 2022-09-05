package com.shirosoftware.sealprogrammingmobile.data

import com.shirosoftware.sealprogrammingmobile.ml.DetectionResult

data class SealDetectionResult(
    val imagePath: String,
    val detectionResults: List<DetectionResult>,
)
