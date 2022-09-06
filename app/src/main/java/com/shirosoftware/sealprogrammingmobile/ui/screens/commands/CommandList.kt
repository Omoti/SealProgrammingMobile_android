package com.shirosoftware.sealprogrammingmobile.ui.screens.commands

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.shirosoftware.sealprogrammingmobile.domain.Seal
import com.shirosoftware.sealprogrammingmobile.ml.DetectionResult

@Composable
fun CommandList(
    detectionResults: List<DetectionResult>,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 12.dp)
    ) {
        Divider(
            modifier = Modifier
                .height(8.dp)
                .width(88.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(reverseLayout = true) {
            items(detectionResults.size) { index ->
                val result = detectionResults[index]
                val seal = sealByLabel(result.label)
                CommandItem(
                    index = index + 1,
                    text = seal.text,
                    color = seal.color
                )
                if ((index + 1) % 10 == 0) {
                    Divider()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CommandItem(index: Int, text: String, color: Color) {
    ListItem(
        modifier = Modifier
            .padding(4.dp),
        icon = {
            Box(
                modifier = Modifier.size(38.dp),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.Filled.Circle,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.fillMaxSize(),
                )
                Text(text = "$index", color = Color.White, textAlign = TextAlign.Center)
            }
        }
    ) {
        Text(text = text)
    }
}

private fun sealByLabel(label: String): Seal {
    return Seal.values().find { it.label == label } ?: Seal.Forward
}