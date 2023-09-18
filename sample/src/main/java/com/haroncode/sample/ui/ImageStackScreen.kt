package com.haroncode.sample.ui

import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.haroncode.lazycardstack.LazyCardStack
import com.haroncode.lazycardstack.items
import com.haroncode.lazycardstack.rememberLazyCardStackState
import com.haroncode.lazycardstack.swiper.SwipeDirection
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ImageStackScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        val scope = rememberCoroutineScope()
        val profiles = remember { profiles() }
        val lazyCardStack = rememberLazyCardStackState(
            animationSpec = tween(400)
        )

        LazyCardStack(
            modifier = Modifier
                .padding(16.dp),
            state = lazyCardStack
        ) {
            items(
                items = profiles,
                key = Profile::imageUrl
            ) { profile ->
                ProfileCard(
                    profile = profile,
                    onClickLeft = {
                        scope.launch { lazyCardStack.animateToNext(SwipeDirection.Left) }
                    },
                    onClickRight = {
                        scope.launch { lazyCardStack.animateToNext(SwipeDirection.Right) }
                    }
                )
            }

            item(
                key = { "end" }
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .dragEnabled(false), // we can disable drag in some cards
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Text(
                            modifier = Modifier
                                .align(Alignment.Center),
                            fontSize = 22.sp,
                            text = "Final unswippable card"
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            onClick = {
                scope.launch { lazyCardStack.animateToBack(SwipeDirection.Up) }
            }
        ) {
            Image(
                imageVector = Icons.Default.Refresh,
                contentDescription = ""
            )
        }
    }
}

@Composable
private fun ProfileCard(
    modifier: Modifier = Modifier,
    profile: Profile,
    onClickLeft: () -> Unit = {},
    onClickRight: () -> Unit = {},
) {
    var isLoading by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .then(modifier),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color(0xFFF1F4FF)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            AsyncImage(
                model = profile.imageUrl,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(MaterialTheme.shapes.small),
                contentDescription = null,
                onLoading = { isLoading = true },
                onSuccess = { isLoading = false }
            )

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = profile.name,
                    color = MaterialTheme.colors.primary,
                    fontSize = 22.sp,
                    modifier = Modifier
                        .padding(16.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        modifier = Modifier,
                        onClick = onClickLeft
                    ) {
                        Image(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = ""
                        )
                    }

                    Button(
                        modifier = Modifier,
                        onClick = onClickRight
                    ) {
                        Image(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = ""
                        )
                    }
                }
            }
        }
    }
}


data class Profile(
    val name: String,
    val imageUrl: String,
)

fun profiles(): List<Profile> {
    return listOf(
        Profile(
            "Kit",
            "https://images.freeimages.com/images/large-previews/5c9/cat-1058028.jpg"
        ),
        Profile(
            "One core kit",
            "https://images.freeimages.com/images/large-previews/466/cat-1401781.jpg"
        ),
        Profile(
            "Dog",
            "https://porodisobak.ru/wp-content/uploads/2021/07/sobaka-porody-shelti-1.jpg"
        ),
        Profile(
            "One more dog",
            "https://canispro.ru/wp-content/uploads/4/c/e/4ced0617acae2442b7c5bd1aa70a96e0.jpg"
        ),
        Profile(
            "Some anime girl",
            "https://anime-fans.ru/wp-content/uploads/2021/04/Krasivye-kartinki-anime-arty-paren-i-devushka_16.jpg"
        ),
    )
}
