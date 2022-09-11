package com.shirosoftware.sealprogrammingmobile.converter

import com.shirosoftware.sealprogrammingmobile.device.SerialCommand
import com.shirosoftware.sealprogrammingmobile.domain.Seal

fun Seal.toSerialCommand(): String {
    return when (this) {
        Seal.Forward -> SerialCommand.forward
        Seal.Back -> SerialCommand.back
        Seal.Left -> SerialCommand.left
        Seal.Right -> SerialCommand.right
        Seal.Stop -> SerialCommand.blink // 停止時に点滅させる
        Seal.Light -> SerialCommand.light
        Seal.Horn -> SerialCommand.horn
    }
}
