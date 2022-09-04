package com.shirosoftware.sealprogrammingmobile.ui.screens.camera

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shirosoftware.sealprogrammingmobile.ui.theme.SealProgrammingMobileTheme

@Composable
fun SwitchCameraButton(modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Icon(
            Icons.Outlined.Sync,
            contentDescription = null,
            tint = Color.White,
            modifier = modifier
                .fillMaxSize()
                .border(width = 2.dp, shape = CircleShape, color = Color.White)
                .padding(8.dp)
        )
    }
}

@Preview
@Composable
fun PreviewSwitchCameraButton() {
    SealProgrammingMobileTheme {
        SwitchCameraButton {}
    }
}
