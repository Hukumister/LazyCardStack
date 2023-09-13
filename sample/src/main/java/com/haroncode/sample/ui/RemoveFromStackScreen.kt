package com.haroncode.sample.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.haroncode.lazycardstack.items
import com.haroncode.lazycardstack.rememberLazyCardStackState
import com.haroncode.lazycardstack.swiper.SwipeDirection
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RemoveFromStackScreen() {
    val list = remember {
        mutableStateOf(listOf("1", "2", "3", "4", "5", "6", "7", "8"))
    }
    val cardStackState = rememberLazyCardStackState()
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyCardStack(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxSize(),
            onSwipedItem = { index, direction ->
                if (direction == SwipeDirection.Left) {
                    list.value = list.value.toMutableList().also { it.removeAt(index) }
                }
            },
            state = cardStackState
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

        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 16.dp)
        ) {
            FloatingActionButton(
                modifier = Modifier
                    .padding(end = 16.dp, bottom = 16.dp),
                onClick = {
                    scope.launch { cardStackState.animateToBack(SwipeDirection.Up) }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null
                )
            }

            FloatingActionButton(
                modifier = Modifier
                    .padding(end = 16.dp, bottom = 16.dp),
                onClick = {
                    val newString = generateRandomString(5)
                    list.value = list.value.toMutableList().apply { add(newString) }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        }
    }
}

fun generateRandomString(length: Int): String {
    val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9') // Define the character pool
    return (1..length)
        .map { Random.nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("")
}
