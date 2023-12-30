package com.haroncode.sample.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp

@Composable
internal fun TextCard(
    modifier: Modifier = Modifier,
    text: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .then(modifier)
    ) {
        Text(
            text = text,
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}
