package com.shirosoftware.sealprogrammingmobile.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.shirosoftware.sealprogrammingmobile.ui.theme.Primary
import com.shirosoftware.sealprogrammingmobile.ui.theme.SealProgrammingMobileTheme

@Composable
fun SegmentedButtons(
    labels: List<String>,
    modifier: Modifier = Modifier,
    color: Color = Primary,
    selectedTextColor: Color = Color.White,
    onSelectionChanged: (index: Int) -> Unit = {},
) {
    var selectedIndex by remember { mutableStateOf(0) }

    val cornerRadius = 20.dp
    Row(modifier = modifier) {
        labels.forEachIndexed { index, label ->
            OutlinedButton(
                border = BorderStroke(width = 1.dp, color = color),
                shape = when (index) {
                    0 -> RoundedCornerShape(
                        topStart = cornerRadius,
                        topEnd = 0.dp,
                        bottomStart = cornerRadius,
                        bottomEnd = 0.dp,
                    )
                    // right outer button
                    labels.size - 1 -> RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = cornerRadius,
                        bottomStart = 0.dp,
                        bottomEnd = cornerRadius,
                    )
                    // middle button
                    else -> RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp,
                    )
                },
                modifier = when (index) {
                    0 ->
                        Modifier
                            .offset(0.dp, 0.dp)
                            .zIndex(if (selectedIndex == index) 1f else 0f)
                    else ->
                        Modifier
                            .offset((-1 * index).dp, 0.dp)
                            .zIndex(if (selectedIndex == index) 1f else 0f)
                }.weight(1.0f),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (selectedIndex == index) color else Color.Transparent,
                ),
                onClick = {
                    selectedIndex = index
                    onSelectionChanged(index)
                },
            ) {
                Text(text = label, color = if (selectedIndex == index) selectedTextColor else color)
            }
        }
    }
}

@Preview
@Composable
fun Preview_SegmentedButtons() {
    SealProgrammingMobileTheme {
        SegmentedButtons(
            labels = listOf("foo", "bar", "hoge"),
            modifier = Modifier.width(300.dp),
            onSelectionChanged = {},
        )
    }
}
