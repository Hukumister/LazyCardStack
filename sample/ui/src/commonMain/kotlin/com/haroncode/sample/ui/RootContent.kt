package com.haroncode.sample.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.haroncode.sample.ui.components.TopBar
import com.haroncode.sample.ui.screen.ImageStackScreen
import com.haroncode.sample.ui.screen.InfinityStackScreen
import com.haroncode.sample.ui.screen.MainScreen
import com.haroncode.sample.ui.screen.RemoveFromStackScreen
import com.haroncode.sample.ui.theme.LazyCardStackTheme

internal enum class Screen {
    Main,
    Infinity,
    Remove,
    Images
}

@Composable
internal fun RootContent(
    paddingValues: PaddingValues = PaddingValues()
) {
    var screen by remember { mutableStateOf(Screen.Main) }

    LazyCardStackTheme {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues)
        ) {
            if (screen != Screen.Main) {
                TopBar(
                    name = screen.name,
                    onBackClick = {
                        screen = Screen.Main
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                )
            }
            when (screen) {
                Screen.Main -> MainScreen(
                    onInfinityClick = { screen = Screen.Infinity },
                    onRemoveClick = { screen = Screen.Remove },
                    onImagesClick = { screen = Screen.Images }
                )

                Screen.Infinity -> InfinityStackScreen()
                Screen.Remove -> RemoveFromStackScreen()
                Screen.Images -> ImageStackScreen()
            }
        }
    }
}
