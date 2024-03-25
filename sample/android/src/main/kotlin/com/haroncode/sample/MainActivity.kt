package com.haroncode.sample

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import com.haroncode.sample.ui.ComposeApp
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.httpFetcher
import io.kamel.core.config.takeFrom
import io.kamel.image.config.Default
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val kamelConfig = remember {
                KamelConfig {
                    takeFrom(KamelConfig.Default)
                    httpFetcher {
                        Logging {
                            logger = CustomAndroidHttpLogger
                        }

                    }
                }
            }

            ComposeApp(
                activity = this,
                kamelConfig = kamelConfig
            )
        }
    }

    private object CustomAndroidHttpLogger : Logger {
        private const val logTag = "CustomAndroidHttpLogger"

        override fun log(message: String) {
            Log.i(logTag, message)
        }
    }
}
