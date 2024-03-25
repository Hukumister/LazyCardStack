package com.haroncode.sample.ui

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalConfiguration
import com.haroncode.lazycardstack.swiper.ComposeWindow
import com.haroncode.lazycardstack.swiper.LocalComposeWindow
import io.kamel.core.config.KamelConfig
import io.kamel.image.config.LocalKamelConfig

@Composable
fun ComposeApp(
    activity: Activity,
    kamelConfig: KamelConfig
) {
    val configuration = LocalConfiguration.current
    val composeWindow = ComposeWindow(configuration.screenWidthDp, configuration.screenHeightDp)

    SetInsetsColors(activity)

    CompositionLocalProvider(
        LocalComposeWindow provides composeWindow,
        LocalKamelConfig provides kamelConfig
    ) {
        RootContent()
    }
}
