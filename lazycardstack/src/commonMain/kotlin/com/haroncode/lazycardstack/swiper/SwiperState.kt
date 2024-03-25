package com.haroncode.lazycardstack.swiper

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animate
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sign
import kotlin.math.sqrt


public class SwiperState(
    private val animationSpec: AnimationSpec<Offset> = SpringSpec(),
    private val horizontalThreshold: (Float) -> Float,
    private val verticalThreshold: (Float) -> Float
) {

    private var maxHeight: Int by mutableIntStateOf(0)

    private var maxWidth: Int by mutableIntStateOf(0)

    public var offset: Offset by mutableStateOf(Offset.Zero)
        private set

    public var scale: Float by mutableFloatStateOf(0.0f)
        private set

    public var rotation: Float by mutableFloatStateOf(0.0f)
        private set

    public var isAnimationRunning: Boolean by mutableStateOf(false)
        private set

    public var isEnabled: Boolean by mutableStateOf(true)

    internal var directions: Set<SwipeDirection> by mutableStateOf(setOf())

    internal var startDragAmount by mutableStateOf(Offset.Zero)

    internal val swiperDraggableState = SwiperDraggableState { delta ->
        offset += delta
        scale = normalize(
            min = 0.0f,
            max = maxWidth.toFloat() / 3,
            value = sqrt(offset.x.pow(2) + offset.y.pow(2)),
            startRange = 0.8f
        )
        rotation = computeRotation(startDragAmount, offset)
    }

    public suspend fun animateToCenter(
        animation: AnimationSpec<Offset> = animationSpec,
    ) {
        try {
            internalAnimateTo(Offset.Zero, animation)
        } catch (ex: CancellationException) {
            internalSnapTo(Offset.Zero)
        }
    }

    public suspend fun animateTo(
        target: SwipeDirection,
        animation: AnimationSpec<Offset> = animationSpec,
    ) {
        try {
            animateToDirection(target, animation)
        } catch (ex: CancellationException) {
            snapToDirection(target)
        }
    }

    public suspend fun snapTo(target: Offset) {
        swiperDraggableState.drag {
            dragBy(target - offset)
        }
    }

    internal fun setMaxWidthAndHeight(height: Int, width: Int) {
        maxHeight = height
        maxWidth = width
    }

    internal suspend fun performFling(): SwipeDirection? {
        val target = computeTarget(offset)
        val realTarget = target
            ?.takeIf { it in directions }
            ?.takeIf { isEnabled }
        if (realTarget != null) {
            try {
                animateToDirection(target)
            } catch (ex: CancellationException) {
                snapToDirection(target)
            }
        } else {
            internalAnimateTo(Offset.Zero, animationSpec)
        }
        return realTarget
    }


    private suspend fun internalSnapTo(
        target: Offset,
        dragPriority: MutatePriority = MutatePriority.Default
    ) {
        swiperDraggableState.drag(dragPriority) {
            dragBy(target - offset)
        }
    }

    internal fun offsetByDirection(direction: SwipeDirection): Offset = when (direction) {
        SwipeDirection.Left -> {
            val distance = -maxWidth - horizontalThreshold(maxWidth.toFloat())
            Offset(distance, offset.y)
        }

        SwipeDirection.Right -> {
            val distance = maxWidth + horizontalThreshold(maxWidth.toFloat())
            Offset(distance, offset.y)
        }

        SwipeDirection.Up -> {
            val distance = -maxHeight - verticalThreshold(maxHeight.toFloat())
            Offset(offset.x, distance)
        }

        SwipeDirection.Down -> {
            val distance = maxHeight + verticalThreshold(maxHeight.toFloat())
            Offset(offset.x, distance)
        }
    }

    private suspend fun animateToDirection(
        target: SwipeDirection,
        animation: AnimationSpec<Offset> = animationSpec,
    ) {
        val targetOffset = offsetByDirection(target)
        internalAnimateTo(targetOffset, animation)
    }

    private suspend fun snapToDirection(target: SwipeDirection) {
        val targetOffset = offsetByDirection(target)
        internalSnapTo(targetOffset, MutatePriority.PreventUserInput)
    }

    private fun computeRotation(
        startDragPosition: Offset,
        offset: Offset
    ): Float {
        val targetRotation = normalize(
            min = 0.0f,
            max = maxWidth.toFloat(),
            value = abs(offset.x),
            startRange = 0f,
            endRange = 15f
        )

        val sign = if (startDragPosition.y < maxHeight.toFloat() / 2) {
            offset.x.sign
        } else {
            -offset.x.sign
        }
        return targetRotation * sign
    }

    private suspend fun internalAnimateTo(target: Offset, animationSpec: AnimationSpec<Offset>) {
        swiperDraggableState.drag {
            try {
                var prevValue = offset
                isAnimationRunning = true
                animate(
                    typeConverter = Offset.VectorConverter,
                    initialValue = offset,
                    targetValue = target,
                    animationSpec = animationSpec
                ) { value, _ ->
                    dragBy(value - prevValue)
                    prevValue = value
                }
            } finally {
                isAnimationRunning = false
            }
        }
    }

    private fun computeTarget(offset: Offset): SwipeDirection? {
        val horizontalRelativeThreshold = abs(horizontalThreshold(maxWidth.toFloat()))
        val verticalRelativeThreshold = abs(verticalThreshold(maxWidth.toFloat()))

        return when {
            offset.x <= 0f && abs(offset.x) > horizontalRelativeThreshold -> SwipeDirection.Left
            offset.x >= 0f && offset.x > horizontalRelativeThreshold -> SwipeDirection.Right
            offset.y <= 0f && abs(offset.y) > verticalRelativeThreshold -> SwipeDirection.Up
            offset.y >= 0f && offset.y > verticalRelativeThreshold -> SwipeDirection.Down
            else -> null
        }
    }

    private fun normalize(
        min: Float,
        max: Float,
        value: Float,
        startRange: Float = 0f,
        endRange: Float = 1f
    ): Float {
        require(startRange < endRange) {
            "startRange must be less than endRange."
        }
        val coercedValue = value.coerceIn(min, max)
        return (coercedValue - min) / (max - min) * (endRange - startRange) + startRange
    }
}


internal fun Modifier.swiper(
    state: SwiperState,
    directions: Set<SwipeDirection> = emptySet(),
    onSwiped: (SwipeDirection) -> Unit = {},
): Modifier = composed {
    val maxWidth = with(LocalComposeWindow.current) {
        LocalDensity.current.run { width.dp.roundToPx() }
    }
    val maxHeight = with(LocalComposeWindow.current) {
        LocalDensity.current.run { height.dp.roundToPx() }
    }

    SideEffect {
        state.directions = directions
        state.setMaxWidthAndHeight(width = maxWidth, height = maxHeight)
    }

    draggableSwiper(
        state = state.swiperDraggableState,
        onDragStopped = {
            val direction = state.performFling()
            if (direction != null) onSwiped(direction)
            state.startDragAmount = Offset.Zero
        },
        onDragStarted = { offset ->
            state.startDragAmount = offset
        }
    )
}

internal fun Modifier.draggableSwiper(
    state: SwiperDraggableState,
    onDragStarted: suspend CoroutineScope.(startedPosition: Offset) -> Unit = {},
    onDragStopped: suspend CoroutineScope.() -> Unit = {},
) = composed {
    val scope = rememberCoroutineScope()
    var velocity by remember { mutableStateOf(Offset(0f, 0f)) }

    pointerInput(Unit) {
        detectDragGestures(
            onDragCancel = { scope.launch { onDragStopped() } },
            onDragEnd = { scope.launch { onDragStopped() } },
            onDrag = { change, dragAmount ->
                if (change.positionChange() != Offset.Zero) change.consume()
                velocity = dragAmount
                scope.launch { state.drag(MutatePriority.UserInput) { dragBy(dragAmount) } }
            },
            onDragStart = { dragAmount -> scope.launch { onDragStarted(dragAmount) } }
        )
    }
}
