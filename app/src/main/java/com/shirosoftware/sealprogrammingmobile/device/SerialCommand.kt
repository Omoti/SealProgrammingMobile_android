package com.shirosoftware.sealprogrammingmobile.device

class SerialCommand {
    companion object {
        // -- !: コマンドバッファのクリア。
        const val clear = "!"

        // -- A: 「リピート」をコマンドバッファに追加。
        const val repeat = "A"

        /// -- B: 「点滅」をコマンドバッファに追加。
        const val blink = "B"

        // -- C: 「ヘッドライト」をコマンドバッファに追加。
        const val light = "C"

        // -- D: 「停止」をコマンドバッファに追加。
        const val stop = "D"

        // -- E: 「ゴール」をコマンドバッファに追加。
        const val goal = "E"

        // -- F: 「クラクション」をコマンドバッファに追加。
        const val horn = "F"

        // -- G: 「右」をコマンドバッファに追加。
        const val right = "G"

        /// -- H: 「後退」をコマンドバッファに追加。
        const val back = "H"

        /// -- I: 「左」をコマンドバッファに追加。
        const val left = "I"

        /// -- J: 「前進」をコマンドバッファに追加。
        const val forward = "J"

        /// -- h: LED の赤レベルを 0%に。
        const val ledRed0 = "h"

        /// -- i: LED の赤レベルを 33%に。
        const val ledRed33 = "i"

        /// -- j: LED の赤レベルを 67%に。
        const val ledRed67 = "j"

        /// -- k: LED の赤レベルを 100%に。
        const val ledRed100 = "k"

        /// -- l: LED の緑レベルを 0%に。
        const val ledGreen0 = "l"

        /// -- m: LED の緑レベルを 33%に。
        const val ledGreen33 = "m"

        /// -- n: LED の緑レベルを 67%に。
        const val ledGreen67 = "n"

        /// -- o: LED の緑レベルを 100%に。
        const val ledGreen100 = "o"

        /// -- p: LED の青レベルを 0%に。
        const val ledBlue0 = "p"

        /// -- q: LED の青レベルを 33%に。
        const val ledBlue33 = "q"

        /// -- r: LED の青レベルを 67%に。
        const val ledBlue67 = "r"

        /// -- s: LED の青レベルを 100%に。
        const val ledBlue100 = "s"
    }
}
