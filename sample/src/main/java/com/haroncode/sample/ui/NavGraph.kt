package com.haroncode.sample.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavGraph(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = "main",
        modifier = Modifier
            .fillMaxSize()
    ) {
        composable("infinity") {
            InfinityStackScreen()
        }

        composable("remove") {
            RemoveFromStackScreen()
        }

        composable("main") {
            MainScreen(navController)
        }
    }
}
