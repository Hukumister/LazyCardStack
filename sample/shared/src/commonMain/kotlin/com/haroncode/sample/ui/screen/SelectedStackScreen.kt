package com.haroncode.sample.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.haroncode.lazycardstack.LazyCardStack
import com.haroncode.lazycardstack.items
import com.haroncode.lazycardstack.rememberLazyCardStackState
import com.haroncode.sample.ui.components.TextCard


@Composable
internal fun SelectedStackScreen() {
    val items = remember {
        mutableStateOf(
            listOf(
                SelectedItem("1", false),
                SelectedItem("2", true),
                SelectedItem("3", false),
                SelectedItem("4", false),
                SelectedItem("5", false),
                SelectedItem("6", true),
            )
        )
    }
    val cardStackState = rememberLazyCardStackState()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyCardStack(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxSize(),
            state = cardStackState
        ) {
            items(
                items = items.value,
                key = { it.id }
            ) { item ->
                TextCard(
                    modifier = Modifier
                        .background(Color.White),
                    text = "${item.id} - ${item.selected}"
                )
            }

            item(
                key = { "last_element" },
            ) {
                TextCard(
                    modifier = Modifier
                        .background(Color.White)
                        .dragEnabled(false),
                    text = "Loading..."
                )
            }
        }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 16.dp),
            onClick = {
                items.value = items.value.map { it.copy(selected = !it.selected) }
            }
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null
            )
        }
    }
}

data class SelectedItem(
    val id: String,
    val selected: Boolean
)
