package com.shirosoftware.sealprogrammingmobile.ui.screens.info

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.shirosoftware.sealprogrammingmobile.BuildConfig
import com.shirosoftware.sealprogrammingmobile.R
import com.shirosoftware.sealprogrammingmobile.ui.theme.Primary

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun InfoScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onShowLicense: () -> Unit,
) {
    val version = BuildConfig.VERSION_NAME

    Scaffold(modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    androidx.compose.material3.Text(
                        text = stringResource(id = R.string.info_title),
                        color = Color.White,
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Primary),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Color.White,
                        )
                    }
                }
            )
        }) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            ListItem(
                text = { Text(text = stringResource(id = R.string.info_license)) },
                modifier = Modifier.clickable {
                    onShowLicense.invoke()
                }
            )
            Divider()
            ListItem(
                text = { Text(text = stringResource(id = R.string.info_version)) },
                secondaryText = { Text(text = version) }
            )
            Divider()
        }
    }
}