package com.haroncode.lazycardstack

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.SpringSpec
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.layout.LazyLayoutItemProvider
import androidx.compose.foundation.lazy.layout.LazyLayoutPrefetchState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.OnGloballyPositionedModifier
import androidx.compose.ui.layout.Remeasurement
import androidx.compose.ui.layout.RemeasurementModifier
import androidx.compose.ui.unit.Constraints
import com.haroncode.lazycardstack.swiper.SwipeDirection
import com.haroncode.lazycardstack.swiper.SwiperState
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Stable
@Composable
fun rememberLazyCardStackState(
    firstVisibleItemIndex: Int = 0,
    horizontalThreshold: (Float) -> Float = { distance -> distance * 0.4f },
    verticalThreshold: (Float) -> Float = { distance -> distance * 0.3f },
    animationSpec: AnimationSpec<Offset> = SpringSpec(),
): LazyCardStackState {
    return rememberSaveable(
        saver = LazyCardStackState.Saver(
            horizontalThreshold = horizontalThreshold,
            verticalThreshold = verticalThreshold,
            animationSpec = animationSpec
        )
    ) {
        LazyCardStackState(
            firstVisibleItemIndex = firstVisibleItemIndex,
            horizontalThreshold = horizontalThreshold,
            verticalThreshold = verticalThreshold,
            animationSpec = animationSpec
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Stable
class LazyCardStackState internal constructor(
    firstVisibleItemIndex: Int = 0,
    private val animationSpec: AnimationSpec<Offset>,
    horizontalThreshold: (Float) -> Float,
    verticalThreshold: (Float) -> Float
) {

    internal val swiperState = SwiperState(
        horizontalThreshold = horizontalThreshold,
        verticalThreshold = verticalThreshold,
        animationSpec = animationSpec
    )

    val offset: Offset get() = swiperState.offset

    val rotation: Float get() = swiperState.rotation

    val scale: Float get() = swiperState.scale

    val isAnimationRunning: Boolean get() = swiperState.isAnimationRunning

    var itemsCount: Int by mutableIntStateOf(0)
        private set

    var visibleItemIndex by mutableIntStateOf(firstVisibleItemIndex)
        private set

    private var lastKnownFirstItemKey: Any? = null

    internal var remeasurement: Remeasurement? by mutableStateOf(null)
        private set


    internal val awaitLayoutModifier = AwaitFirstLayoutModifier()

    internal var premeasureConstraints = Constraints()

    internal val prefetchState = LazyLayoutPrefetchState()

    /**
     * The modifier which provides [remeasurement].
     */
    internal val remeasurementModifier = object : RemeasurementModifier {
        override fun onRemeasurementAvailable(remeasurement: Remeasurement) {
            this@LazyCardStackState.remeasurement = remeasurement
        }
    }

    suspend fun animateToBack(
        fromDirection: SwipeDirection,
        animation: AnimationSpec<Offset> = animationSpec,
    ) {
        awaitLayoutModifier.waitForFirstLayout()

        val nextIndex = visibleItemIndex - 1
        val realIndex = nextIndex.coerceIn(0, itemsCount - 1)

        if (visibleItemIndex == realIndex) return

        visibleItemIndex = realIndex
        lastKnownFirstItemKey = null

        val target = swiperState.offsetByDirection(fromDirection)
        swiperState.snapTo(target)

        swiperState.animateToCenter(animation)
        remeasurement?.forceRemeasure()
    }

    suspend fun snapTo(index: Int) {
        awaitLayoutModifier.waitForFirstLayout()

        val realIndex = index.coerceIn(0, itemsCount - 1)

        visibleItemIndex = realIndex
        lastKnownFirstItemKey = null

        swiperState.snapTo(Offset.Zero)

        prefetchState.schedulePrefetch(visibleItemIndex + 2, premeasureConstraints)
        remeasurement?.forceRemeasure()
    }

    suspend fun animateToNext(
        direction: SwipeDirection,
        animation: AnimationSpec<Offset> = animationSpec,
    ) {
        awaitLayoutModifier.waitForFirstLayout()

        val nextIndex = visibleItemIndex + 1
        val realIndex = nextIndex.coerceIn(0, itemsCount - 1)

        swiperState.animateTo(direction, animation)

        visibleItemIndex = realIndex
        lastKnownFirstItemKey = null

        swiperState.snapTo(Offset.Zero)

        prefetchState.schedulePrefetch(visibleItemIndex + 2, premeasureConstraints)
        remeasurement?.forceRemeasure()
    }

    internal fun applyMeasureResult(result: LazyCardStackMeasureResult) {
        itemsCount = result.itemCount
        lastKnownFirstItemKey = result.currentItem?.key
    }

    internal fun updateScrollPositionIfTheFirstItemWasDeleted(
        itemProvider: LazyLayoutItemProvider,
        index: Int
    ): Int {
        if (itemsCount < itemProvider.itemCount) {
            return index
        }

        val newIndex = itemProvider.findIndexByKey(
            key = lastKnownFirstItemKey,
            lastKnownIndex = index,
        )
        if (index != newIndex) {
            visibleItemIndex = newIndex
        }
        return newIndex
    }

    private fun LazyLayoutItemProvider.findIndexByKey(
        key: Any?,
        lastKnownIndex: Int,
    ): Int {
        if (key == null) {
            // there were no real item during the previous measure
            return lastKnownIndex
        }
        if (lastKnownIndex < itemCount && key == getKey(lastKnownIndex)) {
            // this item is still at the same index
            return lastKnownIndex
        }
        val newIndex = getIndex(key)
        if (newIndex != -1) {
            return newIndex
        }
        // fallback to the previous index if we don't know the new index of the item
        return lastKnownIndex
    }

    companion object {
        /**
         * The default [Saver] implementation for [LazyListState].
         */
        fun Saver(
            animationSpec: AnimationSpec<Offset>,
            horizontalThreshold: (Float) -> Float,
            verticalThreshold: (Float) -> Float
        ) = Saver<LazyCardStackState, Int>(
            save = { it.visibleItemIndex },
            restore = {
                LazyCardStackState(
                    firstVisibleItemIndex = it,
                    horizontalThreshold = horizontalThreshold,
                    verticalThreshold = verticalThreshold,
                    animationSpec = animationSpec
                )
            }
        )
    }
}


internal class AwaitFirstLayoutModifier : OnGloballyPositionedModifier {
    private var wasPositioned = false
    private var continuation: Continuation<Unit>? = null

    suspend fun waitForFirstLayout() {
        if (!wasPositioned) {
            val oldContinuation = continuation
            suspendCoroutine { continuation = it }
            oldContinuation?.resume(Unit)
        }
    }

    override fun onGloballyPositioned(coordinates: LayoutCoordinates) {
        if (!wasPositioned) {
            wasPositioned = true
            continuation?.resume(Unit)
            continuation = null
        }
    }
}

