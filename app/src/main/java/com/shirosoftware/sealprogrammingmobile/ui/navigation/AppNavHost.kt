package com.shirosoftware.sealprogrammingmobile.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.shirosoftware.sealprogrammingmobile.ui.screens.camera.CameraScreen
import com.shirosoftware.sealprogrammingmobile.ui.screens.main.MainScreen
import com.shirosoftware.sealprogrammingmobile.ui.screens.settings.SettingsScreen

@ExperimentalAnimationApi
@Composable
fun AppNavHost(navController: NavHostController) {
    AnimatedNavHost(
        navController = navController,
        startDestination = "main",
    ) {
        composable("main") {
            MainScreen(
                hiltViewModel(),
                onClickCamera = {
                    navController.navigate("camera")
                },
                onClickSettings = {
                    navController.navigate("settings")
                })
        }
        composable("settings") {
            SettingsScreen(hiltViewModel(), onBack = {
                navController.popBackStack()
            })
        }
        composable("camera") {
            CameraScreen(hiltViewModel(), onBack = {
                navController.popBackStack()
            })
        }
    }
}
