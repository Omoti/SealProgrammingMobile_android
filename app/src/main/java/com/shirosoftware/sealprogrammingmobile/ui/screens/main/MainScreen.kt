package com.shirosoftware.sealprogrammingmobile.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.ElectricCar
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.shirosoftware.sealprogrammingmobile.R
import com.shirosoftware.sealprogrammingmobile.ui.components.CircleButton
import com.shirosoftware.sealprogrammingmobile.ui.theme.BackgroundDark
import com.shirosoftware.sealprogrammingmobile.ui.theme.SealProgrammingMobileTheme

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) })
    }) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color.White),
        ) {
            Box(
                modifier = Modifier
                    .aspectRatio(3.0f / 4.0f)
                    .fillMaxWidth()
            ) {
                // TODO : Image
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = modifier
                    .fillMaxSize()
                    .background(BackgroundDark)
            ) {
                CircleButton(
                    Icons.Default.PhotoCamera,
                    stringResource(id = R.string.main_button_camera),
                    onClick = { /*TODO*/ })
                CircleButton(
                    Icons.Default.Bluetooth,
                    stringResource(id = R.string.main_button_connect),
                    onClick = { /*TODO*/ })
                CircleButton(
                    Icons.Default.ElectricCar,
                    stringResource(id = R.string.main_button_send),
                    onClick = { /*TODO*/ })
            }
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    SealProgrammingMobileTheme {
        MainScreen()
    }
}