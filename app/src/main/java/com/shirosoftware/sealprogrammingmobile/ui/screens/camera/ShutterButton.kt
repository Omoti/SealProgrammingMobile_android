package com.shirosoftware.sealprogrammingmobile.ui.screens.camera

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Lens
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shirosoftware.sealprogrammingmobile.ui.theme.SealProgrammingMobileTheme

@Composable
fun ShutterButton(modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            Icons.Sharp.Lens,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(80.dp)
                .border(1.dp, Color.White, CircleShape)
        )
    }
}

@Preview
@Composable
fun PreviewShutterButton() {
    SealProgrammingMobileTheme {
        ShutterButton {}
    }
}
