@file:OptIn(ExperimentalMaterialApi::class)

package com.haroncode.sample.ui.screen

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.haroncode.lazycardstack.LazyCardStack
import com.haroncode.lazycardstack.PagingObserve
import com.haroncode.lazycardstack.items
import com.haroncode.lazycardstack.rememberLazyCardStackState
import com.haroncode.lazycardstack.swiper.SwipeDirection
import com.haroncode.sample.ui.components.TextCard
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun InfinityStackScreen() {
    val list = remember {
        mutableStateOf(listOf("1", "2", "3", "4", "5", "6", "7"))
    }
    val cardStackState = rememberLazyCardStackState(
        animationSpec = tween(1000)
    )
    val scope = rememberCoroutineScope()

    cardStackState.PagingObserve(prefetchCount = 5) {
        val lastValue = list.value.last().toInt()
        val newList = buildList {
            repeat(20) { index -> add((lastValue + index + 1).toString()) }
        }
        list.value += newList
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyCardStack(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxSize(),
            directions = setOf(
                SwipeDirection.Down,
                SwipeDirection.Up,
                SwipeDirection.Left,
                SwipeDirection.Right
            ),
            state = cardStackState,

        ) {
            items(
                items = list.value,
                key = { it.hashCode() }
            ) { text ->
                TextCard(
                    modifier = Modifier
                        .background(Color.White),
                    text = text
                )
            }

            item(
                key = { "loading" }
            ) {
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                    text = "Loading..."
                )
            }
        }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 16.dp),
            onClick = {
                scope.launch { cardStackState.animateToBack(SwipeDirection.Down) }
            }
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null
            )
        }
    }
}


