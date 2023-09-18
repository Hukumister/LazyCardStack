package com.haroncode.sample.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun MainScreen(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Button(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = { navController.navigate("infinity") }
        ) {
            Text(text = "Infinity stack")
        }

        Button(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = { navController.navigate("remove") }
        ) {
            Text(text = "Remove stack")
        }

        Button(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = { navController.navigate("images") }
        ) {
            Text(text = "Images stack")
        }
    }
}
