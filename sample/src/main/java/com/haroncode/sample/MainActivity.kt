package com.haroncode.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.haroncode.sample.ui.theme.LazyCardStackTheme
import com.vedi.moviefilter.ui.common.stack.LazyCardStackScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LazyCardStackTheme {
                LazyCardStackScreen()
            }
        }
    }
}
