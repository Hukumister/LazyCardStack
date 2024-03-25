package com.haroncode.lazycardstack

import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.IntOffset

internal class LazyCardMeasuredItem(
    val key: Any,
    private val relativeIndex: Int,
    private val dragOffset: IntOffset,
    private val scale: Float,
    private val rotation: Float,
    private val placeables: List<Placeable>
) {

    fun place(scope: Placeable.PlacementScope) = with(scope) {
        placeables.forEach { placeable ->
            if (relativeIndex == 0) {
                val isDragEnabled =
                    (placeable.parentData as? DragableEnabledParentData)?.isEnabled ?: true

                val offset = if (isDragEnabled) dragOffset else IntOffset.Zero
                val rotation = if (isDragEnabled) rotation else 0.0f

                placeable.placeRelativeWithLayer(offset, zIndex = 1.0f) { rotationZ = rotation }
            } else {
                placeable.placeRelativeWithLayer(IntOffset.Zero, zIndex = -1.0f) {
                    scaleX = scale
                    scaleY = scale
                }
            }
        }
    }
}

internal class LazyCardStackMeasureResult(
    val currentItem: LazyCardMeasuredItem?,
    val itemCount: Int,
)
