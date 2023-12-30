package com.haroncode.sample.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
internal fun TopBar(
    modifier: Modifier,
    name: String,
    onBackClick: () -> Unit,
) {
    Box(modifier = modifier) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clickable(onClick = onBackClick)
        )
        Text(
            text = name,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
