package com.haroncode.lazycardstack

import androidx.compose.runtime.Composable

public interface LazyCardStackScope {

    public fun items(
        count: Int,
        key: ((index: Int) -> Any),
        contentType: (index: Int) -> Any? = { null },
        itemContent: @Composable LazyCardStackItemScope.(index: Int) -> Unit
    )

    public fun item(
        key: ((index: Int) -> Any),
        contentType: (index: Int) -> Any? = { null },
        itemContent: @Composable LazyCardStackItemScope.(index: Int) -> Unit
    )
}

public inline fun <T> LazyCardStackScope.items(
    items: List<T>,
    noinline key: ((item: T) -> Any),
    noinline contentType: (index: Int) -> Any? = { null },
    crossinline itemContent: @Composable LazyCardStackItemScope.(item: T) -> Unit
): Unit = items(
    count = items.size,
    key = { index: Int -> key(items[index]) },
    contentType = contentType
) { index -> itemContent(items[index]) }

public inline fun <T> LazyCardStackScope.itemsIndexed(
    items: List<T>,
    noinline key: (index: Int, item: T) -> Any,
    crossinline contentType: (index: Int, item: T) -> Any? = { _, _ -> null },
    crossinline itemContent: @Composable LazyCardStackItemScope.(index: Int, item: T) -> Unit
): Unit = items(
    count = items.size,
    key = { index: Int -> key(index, items[index]) },
    contentType = { index -> contentType(index, items[index]) }
) { itemContent(it, items[it]) }
