package com.haroncode.sample.ui.screen

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

@Composable
fun MainScreen(
    onInfinityClick: () -> Unit,
    onRemoveClick: () -> Unit,
    onImagesClick: () -> Unit,
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
            onClick = onInfinityClick
        ) {
            Text(text = "Infinity stack")
        }

        Button(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = onRemoveClick
        ) {
            Text(text = "Remove stack")
        }

        Button(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = onImagesClick
        ) {
            Text(text = "Images stack")
        }
    }
}
