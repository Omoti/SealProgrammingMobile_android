package com.shirosoftware.sealprogrammingmobile.ui.screens.camera

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shirosoftware.sealprogrammingmobile.ui.theme.SealProgrammingMobileTheme

@Composable
fun ShutterButton(modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(4.dp)
            .border(width = 2.dp, shape = CircleShape, color = Color.White)
    ) {
        Button(
            onClick = onClick,
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
            modifier = Modifier
                .fillMaxSize()
                .border(width = 6.dp, shape = CircleShape, color = Color.Black)
        ) {}
    }
}

@Preview
@Composable
fun PreviewShutterButton() {
    SealProgrammingMobileTheme {
        ShutterButton {}
    }
}
