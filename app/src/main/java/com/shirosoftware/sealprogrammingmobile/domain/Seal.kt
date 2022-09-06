package com.shirosoftware.sealprogrammingmobile.domain

import androidx.compose.ui.graphics.Color

enum class Seal(val label: String, val text: String, val color: Color) {
    Forward("forward", "まえ", Color(0xFF489409)),
    Back("back", "うしろ", Color(0xFFD73875)),
    Left("left", "ひだり", Color(0xFF278EC2)),
    Right("right", "みぎ", Color(0xFF987DAC)),
    Stop("stop", "とまる", Color(0xFFEE5505)),
    Light("light", "ライト", Color(0xFFF3C309)),
    Horn("horn", "クラクション", Color(0xFFA1CB2A)),
}
