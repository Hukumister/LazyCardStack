package com.haroncode.sample.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.haroncode.sample.ui.ImageStackScreen
import com.haroncode.sample.ui.InfinityStackScreen
import com.haroncode.sample.ui.MainScreen
import com.haroncode.sample.ui.RemoveFromStackScreen

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

        composable("images") {
            ImageStackScreen()
        }
    }
}
