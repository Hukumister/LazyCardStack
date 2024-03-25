package com.haroncode.lazycardstack

import androidx.compose.foundation.lazy.LazyScopeMarker
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.unit.Density

@Stable
@LazyScopeMarker
public interface LazyCardStackItemScope {

    public fun Modifier.dragEnabled(enable: Boolean): Modifier
}

internal class LazyCardStackItemScopeImpl : LazyCardStackItemScope {

    override fun Modifier.dragEnabled(enable: Boolean): Modifier {
        return then(DragableEnabledParentData(enable))
    }
}

internal class DragableEnabledParentData(
    val isEnabled: Boolean
) : ParentDataModifier {

    override fun Density.modifyParentData(
        parentData: Any?
    ): DragableEnabledParentData = this@DragableEnabledParentData
}


