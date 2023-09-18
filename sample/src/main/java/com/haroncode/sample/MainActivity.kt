package com.haroncode.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.haroncode.sample.ui.navigation.NavGraph
import com.haroncode.sample.ui.theme.LazyCardStackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LazyCardStackTheme {
                val navController = rememberNavController()
                NavGraph(navController)
            }
        }
    }
}
