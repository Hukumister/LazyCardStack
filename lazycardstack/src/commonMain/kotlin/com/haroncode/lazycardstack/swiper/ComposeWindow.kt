package com.haroncode.lazycardstack.swiper

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf

public data class ComposeWindow(val width: Int = 0, val height: Int = 0)

public val LocalComposeWindow: ProvidableCompositionLocal<ComposeWindow> =
    compositionLocalOf { ComposeWindow() }
