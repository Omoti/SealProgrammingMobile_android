package com.shirosoftware.sealprogrammingmobile.converter

import com.shirosoftware.sealprogrammingmobile.domain.Seal
import com.shirosoftware.sealprogrammingmobile.ml.DetectionResult

class DetectionResultsConverter {
    companion object {
        fun convertResultsToCommands(results: List<DetectionResult>): String {
            val commands = results.map {
                sealByLabel(it.label).toSerialCommand()
            }

            return commands.toString()
        }

        private fun sealByLabel(label: String): Seal {
            return Seal.values().find { it.label == label } ?: Seal.Stop
        }
    }
}