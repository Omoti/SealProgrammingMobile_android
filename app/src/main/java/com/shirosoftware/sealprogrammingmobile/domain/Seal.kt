package com.shirosoftware.sealprogrammingmobile.domain

import android.graphics.Color


enum class Seal(val label: String, val text: String, val color: Int) {
    Forward("forward", "まえ", Color.rgb(0, 255, 0)),
    Back("back", "うしろ", Color.rgb(0, 255, 0)),
    Left("left", "ひだり", Color.rgb(0, 255, 0)),
    Right("right", "みぎ", Color.rgb(0, 255, 0)),
    Stop("stop", "とまる", Color.rgb(0, 255, 0)),
    Light("light", "ライト", Color.rgb(0, 255, 0)),
    Horn("horn", "クラクション", Color.rgb(0, 255, 0)),
}
