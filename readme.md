# LazyCardStack
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

Jetpack compose tinder like card stack.

## Installing

Add the dependencies:

```groovy
implementation("io.github.haroncode:lazycardstack:0.0.1")
```

## Demo 

Wait a bit, the gif for the demo is quite large and may take a long time to load:

<img src="https://github.com/Hukumister/LazyCardStack/blob/master/media/sample.gif" width="250">

## How to use?

The library has an API similar to LazyColumn.

```kotlin
val cardStackState = rememberLazyCardStackState()
LazyCardStack(
    modifier = Modifier
        .padding(24.dp)
        .fillMaxSize(),
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
```

The `LazyCardStackState` gives you access to the card's _offset_ so that you can create
advanced animations according to the amount of swiping done.


## How to swipe programmatically?

```kotlin

val state = rememberLazyCardStackState()
val scope = rememberCoroutineScope()

Button(
    onClick = {
        scope.launch { cardStackState.animateToNext(SwipeDirection.Right) }
    }
) {
    Text("Like")
}
```

The `animateToNext()` suspend function will return, as soon as the swipe animation is finished.

You can also specify specific animation settings for swipe:
```kotlin

val state = rememberLazyCardStackState()
val scope = rememberCoroutineScope()

scope.launch {
    cardStackState.animateToNext(
        direction = SwipeDirection.Right,
        animation = tween(500)
    )
}
```

## How to detect that a card has been swiped away?

LazyCardStack has a callback that will be called after swiping the card:

```kotlin
LazyCardStack(
    onSwipedItem = { index, direction -> 
        // handle swipe
    },
    state = cardStackState
)
```

## Can I return previous card?

Indeed, `LazyCardStackState` gives you method to return previous card:

```kotlin

val state = rememberLazyCardStackState()
val scope = rememberCoroutineScope()

scope.launch {
    cardStackState.animateToBack(SwipeDirection.Up)
}

```

you must specify the direction from which the animation of returning the card will occur.

## Sample

More usage examples can be found in sample. 

## License
```
MIT License

Copyright (c) 2023 Nikita Zaltsman (@HaronCode)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
