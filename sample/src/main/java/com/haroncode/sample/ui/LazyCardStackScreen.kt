@file:OptIn(ExperimentalMaterialApi::class)

package com.vedi.moviefilter.ui.common.stack

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.haroncode.lazycardstack.LazyCardStack
import com.haroncode.lazycardstack.items
import com.haroncode.lazycardstack.rememberLazyCardStackState
import com.haroncode.lazycardstack.swiper.SwipeDirection
import com.haroncode.sample.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LazyCardStackScreen() {

    val list = remember {
        mutableStateOf(listOf("1", "2", "3", "4", "5", "6", "7"))
    }
    val cardStackState = rememberLazyCardStackState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {

        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .padding(end = 16.dp),
                onClick = {
                    scope.launch { cardStackState.animateToBack(SwipeDirection.Down) }
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_restore_24),
                    contentDescription = null
                )
            }
        }
    ) {
        LazyCardStack(
            modifier = Modifier
                .padding(24.dp)
                .padding(it)
                .fillMaxSize(),
            state = cardStackState
        ) {
            items(
                items = list.value,
                key = { it.hashCode() }
            ) { text ->
                Card(
                    modifier = Modifier
                        .background(Color.White),
                    text = text
                ) {
                    scope.launch {
                        cardStackState.animateToNext(
                            SwipeDirection.Right,
                            tween(300)
                        )
                    }
                }
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

    }
}


@Composable
fun Card(
    modifier: Modifier = Modifier,
    text: String,
    onClickNext: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .then(modifier)
    ) {
        Text(
            text = text,
            modifier = Modifier
                .align(Alignment.Center)
        )

//        Button(
//            onClick = onClickNext,
//            modifier = Modifier
//                .align(Alignment.BottomCenter)
//        ) {
//            Text(text = "Next")
//        }
    }
}

