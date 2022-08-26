package com.shirosoftware.sealprogrammingmobile.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shirosoftware.sealprogrammingmobile.ui.theme.SealProgrammingMobileTheme

@Composable
fun CircleButton(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    enabled: Boolean = true,
) {
    Button(
        shape = CircleShape,
        onClick = onClick,
        modifier = modifier
            .width(72.dp)
            .height(72.dp),
        colors = colors,
        enabled = enabled,
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                icon,
                contentDescription = text,
                modifier = modifier
                    .width(24.dp)
                    .height(24.dp),
                tint = Color.White,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = text, fontSize = 12.sp, color = Color.White)
        }
    }
}

@Preview
@Composable
fun CircleButtonPreview() {
    SealProgrammingMobileTheme {
        CircleButton(icon = Icons.Default.Camera, text = "カメラ", onClick = { /*TODO*/ })
    }
}