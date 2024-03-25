package com.haroncode.lazycardstack

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.lazy.layout.LazyLayout
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.ThresholdConfig
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.haroncode.lazycardstack.swiper.SwipeDirection
import com.haroncode.lazycardstack.swiper.swiper
import kotlinx.coroutines.launch

@Deprecated("Use LazyCardStack without ThresholdConfig, due to it deprecated")
@OptIn(ExperimentalMaterialApi::class)
@Composable
public fun LazyCardStack(
    modifier: Modifier = Modifier,
    threshold: (Orientation) -> ThresholdConfig = { FractionalThreshold(0.3f) },
    velocityThreshold: Dp = 125.dp,
    directions: Set<SwipeDirection> = setOf(SwipeDirection.Left, SwipeDirection.Right),
    state: LazyCardStackState = rememberLazyCardStackState(),
    onSwipedItem: (Int, SwipeDirection) -> Unit = { _, _ -> },
    content: LazyCardStackScope.() -> Unit
) {
    LazyCardStack(
        modifier = modifier,
        directions = directions,
        state = state,
        onSwipedItem = onSwipedItem,
        content = content
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
public fun LazyCardStack(
    modifier: Modifier = Modifier,
    directions: Set<SwipeDirection> = setOf(SwipeDirection.Left, SwipeDirection.Right),
    state: LazyCardStackState = rememberLazyCardStackState(),
    onSwipedItem: (Int, SwipeDirection) -> Unit = { _, _ -> },
    content: LazyCardStackScope.() -> Unit
) {
    val itemProviderLambda = rememberLazyCardStackItemProviderLambda(state, content)
    val measurePolicy = rememberLazyCardStackMeasurePolicy(state, itemProviderLambda)
    val scope = rememberCoroutineScope()
    LazyLayout(
        itemProvider = itemProviderLambda,
        modifier = Modifier
            .then(state.remeasurementModifier)
            .then(state.awaitLayoutModifier)
            .swiper(
                state = state.swiperState,
                directions = directions,
                onSwiped = { direction ->
                    val currentIndex = state.visibleItemIndex
                    scope.launch {
                        state.snapTo(currentIndex + 1)
                        onSwipedItem(currentIndex, direction)
                    }
                }
            )
            .then(modifier),
        measurePolicy = measurePolicy
    )
}
