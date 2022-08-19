package com.shirosoftware.sealprogrammingmobile.ui.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.ElectricCar
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shirosoftware.sealprogrammingmobile.R
import com.shirosoftware.sealprogrammingmobile.ui.components.CircleButton
import com.shirosoftware.sealprogrammingmobile.ui.theme.SealProgrammingMobileTheme

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) })
    }) {
        Column(verticalArrangement = Arrangement.Bottom, modifier = modifier.fillMaxSize()) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
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