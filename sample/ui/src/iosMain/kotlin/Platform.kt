import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ComposeUIViewController
import com.haroncode.lazycardstack.swiper.ComposeWindow
import com.haroncode.lazycardstack.swiper.LocalComposeWindow
import com.haroncode.sample.ui.RootContent
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow

@OptIn(ExperimentalForeignApi::class)
@Suppress("unused", "FunctionNaming", "FunctionName")
fun MainViewController(window: UIWindow): UIViewController = ComposeUIViewController {
    var safePaddingValues by remember { mutableStateOf(PaddingValues()) }

    LaunchedEffect(window.safeAreaInsets) {
        window.safeAreaInsets.useContents {
            safePaddingValues = PaddingValues(
                top = this.top.dp,
                bottom = this.bottom.dp,
                start = this.left.dp,
                end = this.right.dp,
            )
        }
    }

    val composeWindow by remember(window) {
        val windowInfo = window.frame.useContents {
            ComposeWindow(this.size.width.toInt(), this.size.height.toInt())
        }
        mutableStateOf(windowInfo)
    }

    CompositionLocalProvider(
        LocalComposeWindow provides composeWindow,
    ) {
        RootContent(
            modifier = Modifier
                .padding(safePaddingValues)
        )
    }
}
