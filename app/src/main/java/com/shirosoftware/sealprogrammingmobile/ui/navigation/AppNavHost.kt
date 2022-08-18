package com.shirosoftware.sealprogrammingmobile.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.shirosoftware.sealprogrammingmobile.ui.screens.main.MainScreen

@ExperimentalAnimationApi
@Composable
fun AppNavHost(navController: NavHostController) {
    AnimatedNavHost(
        navController = navController,
        startDestination = "main",
    ) {
        composable("main") {
            MainScreen()
        }
    }
}